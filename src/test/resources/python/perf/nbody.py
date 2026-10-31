# The Great Computer Language Shootout
# http://shootout.alioth.debian.org/
# contributed by Isaac Gouy, tuned by Mike Pall; adapted to the Python subset
import math

sqrt = math.sqrt

PI = 3.141592653589793
SOLAR_MASS = 4 * PI * PI
DAYS_PER_YEAR = 365.24

# a body is [x, y, z, vx, vy, vz, mass]
X, Y, Z, VX, VY, VZ, MASS = 0, 1, 2, 3, 4, 5, 6

jupiter = [4.84143144246472090e+00, -1.16032004402742839e+00, -1.03622044471123109e-01,
           1.66007664274403694e-03 * DAYS_PER_YEAR, 7.69901118419740425e-03 * DAYS_PER_YEAR,
           -6.90460016972063023e-05 * DAYS_PER_YEAR, 9.54791938424326609e-04 * SOLAR_MASS]
saturn = [8.34336671824457987e+00, 4.12479856412430479e+00, -4.03523417114321381e-01,
          -2.76742510726862411e-03 * DAYS_PER_YEAR, 4.99852801234917238e-03 * DAYS_PER_YEAR,
          2.30417297573763929e-05 * DAYS_PER_YEAR, 2.85885980666130812e-04 * SOLAR_MASS]
uranus = [1.28943695621391310e+01, -1.51111514016986312e+01, -2.23307578892655734e-01,
          2.96460137564761618e-03 * DAYS_PER_YEAR, 2.37847173959480950e-03 * DAYS_PER_YEAR,
          -2.96589568540237556e-05 * DAYS_PER_YEAR, 4.36624404335156298e-05 * SOLAR_MASS]
neptune = [1.53796971148509165e+01, -2.59193146099879641e+01, 1.79258772950371181e-01,
           2.68067772490389322e-03 * DAYS_PER_YEAR, 1.62824170038242295e-03 * DAYS_PER_YEAR,
           -9.51592254519715870e-05 * DAYS_PER_YEAR, 5.15138902046611451e-05 * SOLAR_MASS]
sun = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, SOLAR_MASS]

def advance(bodies, dt):
    n = len(bodies)
    for i in range(n):
        bi = bodies[i]
        for j in range(i + 1, n):
            bj = bodies[j]
            dx = bi[X] - bj[X]
            dy = bi[Y] - bj[Y]
            dz = bi[Z] - bj[Z]
            distance = sqrt(dx * dx + dy * dy + dz * dz)
            mag = dt / (distance * distance * distance)
            bi[VX] -= dx * bj[MASS] * mag
            bi[VY] -= dy * bj[MASS] * mag
            bi[VZ] -= dz * bj[MASS] * mag
            bj[VX] += dx * bi[MASS] * mag
            bj[VY] += dy * bi[MASS] * mag
            bj[VZ] += dz * bi[MASS] * mag
    for b in bodies:
        b[X] += dt * b[VX]
        b[Y] += dt * b[VY]
        b[Z] += dt * b[VZ]

def energy(bodies):
    e = 0.0
    n = len(bodies)
    for i in range(n):
        bi = bodies[i]
        e += 0.5 * bi[MASS] * (bi[VX] * bi[VX] + bi[VY] * bi[VY] + bi[VZ] * bi[VZ])
        for j in range(i + 1, n):
            bj = bodies[j]
            dx = bi[X] - bj[X]
            dy = bi[Y] - bj[Y]
            dz = bi[Z] - bj[Z]
            e -= (bi[MASS] * bj[MASS]) / sqrt(dx * dx + dy * dy + dz * dz)
    return e

def offset_momentum(bodies):
    px = 0.0
    py = 0.0
    pz = 0.0
    for b in bodies:
        px += b[VX] * b[MASS]
        py += b[VY] * b[MASS]
        pz += b[VZ] * b[MASS]
    bodies[0][VX] = -px / SOLAR_MASS
    bodies[0][VY] = -py / SOLAR_MASS
    bodies[0][VZ] = -pz / SOLAR_MASS

def main(n):
    bodies = [sun, jupiter, saturn, uranus, neptune]
    offset_momentum(bodies)
    print("%0.9f" % energy(bodies))
    for i in range(n):
        advance(bodies, 0.01)
    print("%0.9f" % energy(bodies))

main(1000)
