# The Great Computer Language Shootout
# http://shootout.alioth.debian.org/
# contributed by Isaac Gouy, tuned by Mike Pall

sqrt = math.sqrt

PI = 3.141592653589793
SOLAR_MASS = 4 * PI * PI
DAYS_PER_YEAR = 365.24

Jupiter = {
    "x": 4.84143144246472090e+00,
    "y": -1.16032004402742839e+00,
    "z": -1.03622044471123109e-01,
    "vx": 1.66007664274403694e-03 * DAYS_PER_YEAR,
    "vy": 7.69901118419740425e-03 * DAYS_PER_YEAR,
    "vz": -6.90460016972063023e-05 * DAYS_PER_YEAR,
    "mass": 9.54791938424326609e-04 * SOLAR_MASS,
}

Saturn = {
    "x": 8.34336671824457987e+00,
    "y": 4.12479856412430479e+00,
    "z": -4.03523417114321381e-01,
    "vx": -2.76742510726862411e-03 * DAYS_PER_YEAR,
    "vy": 4.99852801234917238e-03 * DAYS_PER_YEAR,
    "vz": 2.30417297573763929e-05 * DAYS_PER_YEAR,
    "mass": 2.85885980666130812e-04 * SOLAR_MASS,
}

Uranus = {
    "x": 1.28943695621391310e+01,
    "y": -1.51111514016986312e+01,
    "z": -2.23307578892655734e-01,
    "vx": 2.96460137564761618e-03 * DAYS_PER_YEAR,
    "vy": 2.37847173959480950e-03 * DAYS_PER_YEAR,
    "vz": -2.96589568540237556e-05 * DAYS_PER_YEAR,
    "mass": 4.36624404335156298e-05 * SOLAR_MASS,
}

Neptune = {
    "x": 1.53796971148509165e+01,
    "y": -2.59193146099879641e+01,
    "z": 1.79258772950371181e-01,
    "vx": 2.68067772490389322e-03 * DAYS_PER_YEAR,
    "vy": 1.62824170038242295e-03 * DAYS_PER_YEAR,
    "vz": -9.51592254519715870e-05 * DAYS_PER_YEAR,
    "mass": 5.15138902046611451e-05 * SOLAR_MASS,
}

Sun = {"x": 0, "y": 0, "z": 0,
       "vx": 0, "vy": 0, "vz": 0, "mass": SOLAR_MASS}

def advance(bodies, nbody, dt):
    for i in range(1, nbody + 1):
        bi = bodies[i]
        bix, biy, biz, bimass = bi.x, bi.y, bi.z, bi.mass
        bivx, bivy, bivz = bi.vx, bi.vy, bi.vz
        for j in range(i + 1, nbody + 1):
            bj = bodies[j]
            dx, dy, dz = bix - bj.x, biy - bj.y, biz - bj.z
            distance = sqrt(dx * dx + dy * dy + dz * dz)
            mag = dt / (distance * distance * distance)
            bim, bjm = bimass * mag, bj.mass * mag
            bivx -= dx * bjm
            bivy -= dy * bjm
            bivz -= dz * bjm
            bj.vx += dx * bim
            bj.vy += dy * bim
            bj.vz += dz * bim
        bi.vx = bivx
        bi.vy = bivy
        bi.vz = bivz
    for i in range(1, nbody + 1):
        bi = bodies[i]
        bi.x += dt * bi.vx
        bi.y += dt * bi.vy
        bi.z += dt * bi.vz


def energy(bodies, nbody):
    e = 0
    for i in range(1, nbody + 1):
        bi = bodies[i]
        vx, vy, vz, bim = bi.vx, bi.vy, bi.vz, bi.mass
        e += 0.5 * bim * (vx * vx + vy * vy + vz * vz)
        for j in range(i + 1, nbody + 1):
            bj = bodies[j]
            dx, dy, dz = bi.x - bj.x, bi.y - bj.y, bi.z - bj.z
            distance = sqrt(dx * dx + dy * dy + dz * dz)
            e -= (bim * bj.mass) / distance
    return e


def offsetMomentum(b, nbody):
    px, py, pz = 0, 0, 0
    for i in range(1, nbody + 1):
        bi = b[i]
        bim = bi.mass
        px += bi.vx * bim
        py += bi.vy * bim
        pz += bi.vz * bim
    b[1].vx = -px / SOLAR_MASS
    b[1].vy = -py / SOLAR_MASS
    b[1].vz = -pz / SOLAR_MASS

def main():
    N = tonumber(arg and arg[1]) or 1000
    bodies = [Sun, Jupiter, Saturn, Uranus, Neptune]
    nbody = len(bodies)

    offsetMomentum(bodies, nbody)
    io.write(string.format("%0.9f", energy(bodies, nbody)), "\n")
    for i in range(1, N + 1): advance(bodies, nbody, 0.01)
    io.write(string.format("%0.9f", energy(bodies, nbody)), "\n")

main()
