'use strict';

const fs = require('fs');
const http = require('http');
const path = require('path');

function send(res, body, contentType) {
  if (contentType)
    res.setHeader('content-type', contentType);
  res.end(body);
}

function tryServeMountedFile(res, requestPath, mount) {
  if (!requestPath.startsWith(mount.prefix))
    return false;

  const relativePath = requestPath.slice(mount.prefix.length).replace(/^\/+/, '');
  const filePath = path.resolve(mount.dir, relativePath);
  const dirPrefix = mount.dir.endsWith(path.sep) ? mount.dir : mount.dir + path.sep;
  if (filePath !== mount.dir && !filePath.startsWith(dirPrefix))
    return false;
  if (!fs.existsSync(filePath) || !fs.statSync(filePath).isFile())
    return false;

  res.setHeader('content-type', mount.contentType || 'application/javascript');
  res.setHeader('cache-control', 'no-store');
  fs.createReadStream(filePath).pipe(res);
  return true;
}

function createMountedServer(options) {
  const routes = options.routes || {};
  const mounts = (options.mounts || []).map(mount => ({
    prefix: mount.prefix,
    dir: path.resolve(mount.dir),
    contentType: mount.contentType,
  }));

  return http.createServer((req, res) => {
    const requestPath = decodeURIComponent((req.url || '').split('?')[0]);
    if (typeof options.onRequest === 'function')
      options.onRequest(requestPath, req);

    const route = routes[requestPath];
    if (route) {
      if (typeof route === 'function')
        return route(req, res, requestPath);
      return send(res, route.body, route.contentType);
    }

    for (const mount of mounts)
      if (tryServeMountedFile(res, requestPath, mount))
        return;

    res.statusCode = 404;
    res.end();
  });
}

function startServer(server, host) {
  return new Promise(resolve => {
    server.listen(0, host || '127.0.0.1', () => resolve(server.address().port));
  });
}

module.exports = {
  createMountedServer,
  startServer,
};
