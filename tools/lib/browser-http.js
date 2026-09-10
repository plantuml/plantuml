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
  if (relativePath.split('/').includes('..'))
    return false;
  const filePath = path.resolve(mount.dir, relativePath);
  const relativeCheck = path.relative(mount.dir, filePath);
  if (relativeCheck === '..'
    || relativeCheck.startsWith('..' + path.sep) || path.isAbsolute(relativeCheck))
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
    let requestPath;
    try {
      requestPath = decodeURIComponent((req.url || '').split('?')[0]);
    } catch (e) {
      res.statusCode = 400;
      return res.end();
    }
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
  return new Promise((resolve, reject) => {
    const onError = err => {
      server.off('listening', onListening);
      reject(err);
    };
    const onListening = () => {
      server.off('error', onError);
      resolve(server.address().port);
    };
    server.once('error', onError);
    server.once('listening', onListening);
    server.listen(0, host || '127.0.0.1');
  });
}

module.exports = {
  createMountedServer,
  startServer,
};
