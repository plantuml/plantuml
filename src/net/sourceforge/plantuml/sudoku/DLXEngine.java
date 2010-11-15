/****************************************************************************
 * DLXEngine.java
 *
 * Created on den 30 december 2005, 01:04
 *
 * DLXEngine
 * Sudoku puzzle generator and solver based on the suexg and suexk by
 * Gunter Stertenbrink. Suexg and suexk are C implementations of the
 * Dancing Links algorithm by Donald Knuth and optimized for performance
 * which means that certain cleanup work has been done. There is still
 * lots of these activities left to do, however, the code is nasty and
 * hard to read - but extremely efficient.
 *
 * The code is public domain so feel free to use it.
 *****************************************************************************/

package net.sourceforge.plantuml.sudoku;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/*******************************************************************************
 * dlx_solver solve any Sudoku in a fraction of a second. Input is a string of
 * dots and digits representing the puzzle to solve and output is the solved
 * puzzle.
 * 
 * @author Rolf Sandberg
 ******************************************************************************/
class dlx_solver {
	static final int M = 8; // change this for larger grids. Use symbols as in
							// L[] below
	static final int M2 = M * M;
	static final int M4 = M2 * M2;

	/** Pseudo-random number generator */
	long MWC() {
		return random.nextLong();
	}

	int A0[][] = new int[M2 + 9][M2 + 9], A[][] = new int[M2 + 9][M2 + 9], Rows[] = new int[4 * M4 + 9],
			Cols[] = new int[M2 * M4 + 9], Row[][] = new int[4 * M4 + 9][M2 + 9];
	int Col[][] = new int[M2 * M4 + 9][5], Ur[] = new int[M2 * M4 + 9], Uc[] = new int[4 * M4 + 9], V[] = new int[M2
			* M4 + 9];
	int C[] = new int[M4 + 9], I[] = new int[M4 + 9], T[] = new int[M2 * M4 + 9], P[] = new int[M2 * M4 + 9];
	int Mr[] = { 0, 1, 63, 1023, 4095, 16383, 46655, 131071, 262143 };
	int Mc[] = { 0, 1, 63, 511, 1023, 4095, 8191, 16383, 16383 };
	int Mw[] = { 0, 1, 3, 15, 15, 31, 63, 63, 63 };

	int nocheck = 0, max, _try_;
	final int rnd = 0;
	int min, clues, gu, tries;
	long Node[] = new long[M4 + 9];
	long nodes, tnodes, solutions, vmax, smax, time0, time1, t1, x1;
	double xx, yy;
	int q, a, p, i, i1, j, k, l, r, r1, c, c1, c2, n, N = 0, N2, N4, m, m0, m1, x, y, s;
	char L[] = { '.', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'#', '*', '~' };

	/** State machine states */
	static final int M6 = 10;
	static final int M7 = 11;
	static final int RESTART = 12;
	static final int M22 = 13;
	static final int M3 = 14;
	static final int M44 = 15;
	static final int NEXT_TRY = 16;
	static final int END = 30;

	/**
	 * Solver function. Input parameter: A puzzle to solve Output: The solved
	 * puzzle
	 */
	String solve(String puzzle) {
		String result = new String();
		int STATE = M6;

		vmax = 4000000;
		smax = 25;
		p = 1;
		q = 0;

		if (q > 0) {
			vmax = 99999999;
			smax = 99999999;
		}

		N = 3;
		N2 = N * N;
		N4 = N2 * N2;
		m = 4 * N4;
		n = N2 * N4;

		if (puzzle.length() < N4) {
			return "Error, puzzle incomplete";
		}

		while (STATE != END) {
			switch (STATE) {
			case M6:
				clues = 0;
				i = 0;
				for (x = 0; x < N2; x++)
					for (y = 0; y < N2; y++) {
						c = puzzle.charAt(x * N2 + y);
						j = 0;

						if (c == '-' || c == '.' || c == '0' || c == '*') {
							A0[x][y] = j;
							i++;
						} else {
							while (L[j] != c && j <= N2)
								j++;

							if (j <= N2) {
								A0[x][y] = j;
								if (j > 0)
									clues++;
								i++;
							}
						}
					}

				if (clues == N4) {
					clues--;
					A0[1][1] = 0;
				}

				if (p < 8) {
					for (i = 0; i <= N4; i++)
						Node[i] = 0;
				}
				tnodes = 0;

			case RESTART:
				r = 0;
				for (x = 1; x <= N2; x++)
					for (y = 1; y <= N2; y++)
						for (s = 1; s <= N2; s++) {
							r++;
							Cols[r] = 4;
							Col[r][1] = x * N2 - N2 + y;
							Col[r][4] = (N * ((x - 1) / N) + (y - 1) / N) * N2 + s + N4;

							Col[r][3] = x * N2 - N2 + s + N4 * 2;
							Col[r][2] = y * N2 - N2 + s + N4 * 3;
						}
				for (c = 1; c <= m; c++)
					Rows[c] = 0;

				for (r = 1; r <= n; r++)
					for (c = 1; c <= Cols[r]; c++) {
						x = Col[r][c];
						Rows[x]++;
						Row[x][Rows[x]] = r;
					}

				for (x = 0; x < N2; x++)
					for (y = 0; y < N2; y++)
						A[x][y] = A0[x][y];

				for (i = 0; i <= n; i++)
					Ur[i] = 0;
				for (i = 0; i <= m; i++)
					Uc[i] = 0;

				solutions = 0;

				for (x = 1; x <= N2; x++)
					for (y = 1; y <= N2; y++)
						if (A[x - 1][y - 1] > 0) {
							r = x * N4 - N4 + y * N2 - N2 + A[x - 1][y - 1];

							for (j = 1; j <= Cols[r]; j++) {
								c1 = Col[r][j];
								if (Uc[c1] > 0 && nocheck == 0) {
									STATE = NEXT_TRY;
									break;
								}

								Uc[c1]++;

								for (k = 1; k <= Rows[c1]; k++) {
									r1 = Row[c1][k];
									Ur[r1]++;
								}
							}
							if (STATE == NEXT_TRY)
								break;
						}
				if (STATE == NEXT_TRY)
					break;

				if (rnd > 0 && rnd != 17 && rnd != 18)
					shuffle();

				for (c = 1; c <= m; c++) {
					V[c] = 0;
					for (r = 1; r <= Rows[c]; r++)
						if (Ur[Row[c][r]] == 0)
							V[c]++;
				}

				i = clues;
				nodes = 0;
				m0 = 0;
				m1 = 0;
				gu = 0;
				solutions = 0;

			case M22:
				i++;
				I[i] = 0;
				min = n + 1;
				if (i > N4 || m0 > 0) {
					STATE = M44;
					break;
				}
				if (m1 > 0) {
					C[i] = m1;
					STATE = M3;
					break;
				}
				for (c = 1; c <= m; c++)
					if (Uc[c] == 0) {
						if (V[c] <= min)
							c1 = c;
						if (V[c] < min) {
							min = V[c];
							C[i] = c;
							if (min < 2) {
								STATE = M3;
								break;
							}
						}
					}
				if (STATE == M3)
					break;

				gu++;
				if (min > 2) {
					STATE = M3;
					break;
				}

				if ((rnd & 255) == 18)
					if ((nodes & 1) > 0) {
						c = m + 1;
						c--;
						while (Uc[c] > 0 || V[c] != 2)
							c--;
						C[i] = c;
					}

				if ((rnd & 255) == 17) {
					c1 = (int) (MWC() & Mc[N]);
					while (c1 >= m)
						c1 = (int) (MWC() & Mc[N]);
					c1++;

					for (c = c1; c <= m; c++)
						if (Uc[c] == 0)
							if (V[c] == 2) {
								C[i] = c;
								STATE = M3;
								break;
							}
					for (c = 1; c < c1; c++)
						if (Uc[c] == 0)
							if (V[c] == 2) {
								C[i] = c;
								STATE = M3;
								break;
							}
				}

			case M3:
				c = C[i];
				I[i]++;
				if (I[i] > Rows[c]) {
					STATE = M44;
					break;
				}

				r = Row[c][I[i]];
				if (Ur[r] > 0) {
					STATE = M3;
					break;
				}
				m0 = 0;
				m1 = 0;

				if (q > 0 && i > 32 && i < 65)
					if ((MWC() & 127) < q) {
						STATE = M3;
						break;
					}

				k = N4;
				x = (r - 1) / k + 1;
				y = ((r - 1) % k) / j + 1;
				s = (r - 1) % j + 1;

				if ((p & 1) > 0) {
					j = N2;
					k = N4;
					x = (r - 1) / k + 1;
					y = ((r - 1) % k) / j + 1;
					s = (r - 1) % j + 1;
					A[x - 1][y - 1] = s;
					if (i == k) {
						for (x = 0; x < j; x++)
							for (y = 0; y < j; y++)
								result = result.concat(String.valueOf(L[A[x][y]]));
						result = result.concat(" #\n");
					}
				}

				for (j = 1; j <= Cols[r]; j++) {
					c1 = Col[r][j];
					Uc[c1]++;
				}

				for (j = 1; j <= Cols[r]; j++) {
					c1 = Col[r][j];

					for (k = 1; k <= Rows[c1]; k++) {
						r1 = Row[c1][k];
						Ur[r1]++;
						if (Ur[r1] == 1)
							for (l = 1; l <= Cols[r1]; l++) {
								c2 = Col[r1][l];
								V[c2]--;

								if (Uc[c2] + V[c2] < 1)
									m0 = c2;
								if (Uc[c2] == 0 && V[c2] < 2)
									m1 = c2;
							}
					}
				}
				Node[i]++;
				tnodes++;
				nodes++;
				if (rnd > 99 && nodes > rnd) {
					STATE = RESTART;
					break;
				}
				if (i == N4)
					solutions++;

				if (solutions >= smax) {
					System.out.println("smax xolutions found");
					if (_try_ == 1)
						System.out.print("+");
					STATE = NEXT_TRY;
					break;
				}
				if (tnodes > vmax) {
					if (_try_ == 1)
						System.out.print("-");
					STATE = NEXT_TRY;
					break;
				}
				STATE = M22;
				break;

			case M44:
				i--;
				c = C[i];
				r = Row[c][I[i]];
				if (i == clues) {
					STATE = NEXT_TRY;
					break;
				}

				for (j = 1; j <= Cols[r]; j++) {
					c1 = Col[r][j];
					Uc[c1]--;

					for (k = 1; k <= Rows[c1]; k++) {
						r1 = Row[c1][k];
						Ur[r1]--;

						if (Ur[r1] == 0)
							for (l = 1; l <= Cols[r1]; l++) {
								c2 = Col[r1][l];
								V[c2]++;
							}
					}
				}
				if (p > 0) {
					j = N2;
					k = N4;
					x = (r - 1) / k + 1;
					y = ((r - 1) % k) / j + 1;
					s = (r - 1) % j + 1;
					A[x - 1][y - 1] = 0;
				}
				if (i > clues) {
					STATE = M3;
					break;
				}

			case NEXT_TRY:
				time1 = System.currentTimeMillis();
				x1 = time1 - time0;

				time0 = time1;

				if (q > 0) {
					xx = 128;
					yy = 128 - q;
					xx = xx / yy;
					yy = solutions;
					for (i = 1; i < 33; i++)
						yy = yy * xx;
					System.out.println("clues: " + clues + " estimated solutions:" + yy + " time " + x1 + "ms");

					STATE = END;
					break;
				}
				if ((p == 0 || p == 1) && tnodes <= 999999) {
					if (solutions >= smax)
						result = result.concat("More than " + solutions + " solutions ( bad sudoku!! ), rating "
								+ (100 * tnodes / solutions) + ", time " + x1 + " ms");
					else if (solutions == 1)
						result = result.concat(solutions + " solution, rating " + (100 * tnodes) + ", time " + x1
								+ " ms");
					else if (solutions == 0)
						result = result.concat("0 solutions, no rating possible, time " + x1 + " ms");
					else
						result = result.concat(solutions + " solutions ( bad sudoku!! ), rating "
								+ (100 * tnodes / solutions) + ", time " + x1 + " ms");

					STATE = END;
					break;
				}
				if (p == 6) {
					System.out.println(solutions);
					STATE = END;
					break;
				}
				if (p == 0 || p == 1) {
					System.out.println(solutions + " solution(s), rating " + (100 * tnodes) + ", time " + x1 + "ms");
				}
				if (p > 5) {
					x = 0;
					for (i = 1; i <= N4; i++) {
						x += Node[i];
						System.out.print(Node[i]);
						if (i % 9 == 0)
							System.out.println();
					}
					System.out.println(x);
				}
				STATE = END;
				break;
			} // end of switch statement
		} // end of while loop
		return result;
	}

	/**
	 * Helper function.
	 */
	int shuffle() {
		for (i = 1; i <= m; i++) {
			a = (int) ((MWC() >> 8) & Mc[N]);
			while (a >= i)
				a = (int) ((MWC() >> 8) & Mc[N]);
			a++;
			P[i] = P[a];
			P[a] = i;
		}

		for (c = 1; c <= m; c++) {
			Rows[c] = 0;
			T[c] = Uc[c];
		}

		for (c = 1; c <= m; c++)
			Uc[P[c]] = T[c];

		for (r = 1; r <= n; r++)
			for (i = 1; i <= Cols[r]; i++) {
				c = P[Col[r][i]];
				Col[r][i] = c;
				Rows[c]++;
				Row[c][Rows[c]] = r;
			}

		for (i = 1; i <= n; i++) {
			a = (int) ((MWC() >> 8) & Mr[N]);
			while (a >= i)
				a = (int) ((MWC() >> 8) & Mr[N]);
			a++;
			P[i] = P[a];
			P[a] = i;
		}

		for (r = 1; r <= n; r++) {
			Cols[r] = 0;
			T[r] = Ur[r];
		}

		for (r = 1; r <= n; r++)
			Ur[P[r]] = T[r];

		for (c = 1; c <= m; c++)
			for (i = 1; i <= Rows[c]; i++) {
				r = P[Row[c][i]];
				Row[c][i] = r;
				Cols[r]++;
				Col[r][Cols[r]] = c;
			}

		for (r = 1; r <= n; r++) {
			for (i = 1; i <= Cols[r]; i++) {
				a = (int) ((MWC() >> 8) & 7);
				while (a >= i)
					a = (int) ((MWC() >> 8) & 7);
				a++;
				P[i] = P[a];
				P[a] = i;
			}

			for (i = 1; i <= Cols[r]; i++)
				T[i] = Col[r][P[i]];

			for (i = 1; i <= Cols[r]; i++)
				Col[r][i] = T[i];
		}

		for (c = 1; c <= m; c++) {
			for (i = 1; i <= Rows[c]; i++) {
				a = (int) ((MWC() >> 8) & Mw[N]);
				while (a >= i)
					a = (int) ((MWC() >> 8) & Mw[N]);
				a++;
				P[i] = P[a];
				P[a] = i;
			}

			for (i = 1; i <= Rows[c]; i++)
				T[i] = Row[c][P[i]];

			for (i = 1; i <= Rows[c]; i++)
				Row[c][i] = T[i];
		}
		return 0;
	}

	private final Random random;

	/** Creates a new instance of dlx_solver */
	public dlx_solver(Random random) {
		this.random = random;
	}
}

/*******************************************************************************
 * dlx_generator generate single solution locally minimized Sudoku puzzles.
 * Locally minimized means that all keys that can be removed without creating a
 * degenerate Sudoku (multiple solutions) are removed.
 ******************************************************************************/
class dlx_generator {
	long MWC() {
		return random.nextLong();
	}

	int Rows[] = new int[325], Cols[] = new int[730], Row[][] = new int[325][10], Col[][] = new int[730][5],
			Ur[] = new int[730], Uc[] = new int[325], V[] = new int[325], W[] = new int[325];
	int P[] = new int[88], A[] = new int[88], C[] = new int[88], I[] = new int[88], Two[] = new int[888];
	char B[] = { '0', '1', '1', '1', '2', '2', '2', '3', '3', '3', '1', '1', '1', '2', '2', '2', '3', '3', '3', '1',
			'1', '1', '2', '2', '2', '3', '3', '3', '4', '4', '4', '5', '5', '5', '6', '6', '6', '4', '4', '4', '5',
			'5', '5', '6', '6', '6', '4', '4', '4', '5', '5', '5', '6', '6', '6', '7', '7', '7', '8', '8', '8', '9',
			'9', '9', '7', '7', '7', '8', '8', '8', '9', '9', '9', '7', '7', '7', '8', '8', '8', '9', '9', '9' };
	char H[][] = new char[326][7];
	long c2, w;
	int b, f, s1, m0, c1, r1, l, i1, m1, m2, a, p, i, j, k, r, c, d, n = 729, m = 324, x, y, s, z, fi;
	int mi1, mi2, q7, part, nt, rate, nodes, solutions, min, samples, sam1, clues;
	char L[] = { '.', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/** State machine states */
	static final int M0S = 10;
	static final int M0 = 11;
	static final int MR1 = 12;
	static final int MR3 = 13;
	static final int MR4 = 14;
	static final int M2 = 15;
	static final int M3 = 16;
	static final int M4 = 17;
	static final int M9 = 18;
	static final int MR = 19;
	static final int END = 20;
	static final int M6 = 21;

	/** Set to true to generate debug output */
	boolean DBG = false;

	/** Output trace messages */
	void dbg(String s) {
		if (DBG)
			System.out.println(s);
	}

	private final Random random;

	public dlx_generator(Random random) {
		dbg("In constructor");
		this.random = random;
	}

	/**
	 * Save the generated Sudoku to a file.
	 */
	void saveSudokuToFile(String s) {
		FileOutputStream FO = null;
		byte[] buffer = new byte[s.length() + 1];
		int i = 0;

		while (i < s.length()) {
			buffer[i] = (byte) s.charAt(i);
			i++;
		}

		try {
			FO = new FileOutputStream("generated_sudoku.sdk");
			FO.write(buffer);
			FO.close();
		} catch (IOException IOE) {
			// Well, well, well....
			return;
		}
	}

	/**
	 * Initialization code for both generate() and rate()
	 */
	void initialize() {
		for (i = 0; i < 888; i++) {
			j = 1;
			while (j <= i)
				j += j;
			Two[i] = j - 1;
		}

		r = 0;
		for (x = 1; x <= 9; x++)
			for (y = 1; y <= 9; y++)
				for (s = 1; s <= 9; s++) {
					r++;
					Cols[r] = 4;
					Col[r][1] = x * 9 - 9 + y;
					Col[r][2] = (B[x * 9 - 9 + y] - 48) * 9 - 9 + s + 81;
					Col[r][3] = x * 9 - 9 + s + 81 * 2;
					Col[r][4] = y * 9 - 9 + s + 81 * 3;
				}

		for (c = 1; c <= m; c++)
			Rows[c] = 0;

		for (r = 1; r <= n; r++)
			for (c = 1; c <= Cols[r]; c++) {
				a = Col[r][c];
				Rows[a]++;
				Row[a][Rows[a]] = r;
			}

		c = 0;
		for (x = 1; x <= 9; x++)
			for (y = 1; y <= 9; y++) {
				c++;
				H[c][0] = 'r';
				H[c][1] = L[x];
				H[c][2] = 'c';
				H[c][3] = L[y];
				H[c][4] = 0;
			}

		c = 81;
		for (b = 1; b <= 9; b++)
			for (s = 1; s <= 9; s++) {
				c++;
				H[c][0] = 'b';
				H[c][1] = L[b];
				H[c][2] = 's';
				H[c][3] = L[s];
				H[c][4] = 0;
			}

		c = 81 * 2;
		for (x = 1; x <= 9; x++)
			for (s = 1; s <= 9; s++) {
				c++;
				H[c][0] = 'r';
				H[c][1] = L[x];
				H[c][2] = 's';
				H[c][3] = L[s];
				H[c][4] = 0;
			}

		c = 81 * 3;
		for (y = 1; y <= 9; y++)
			for (s = 1; s <= 9; s++) {
				c++;
				H[c][0] = 'c';
				H[c][1] = L[y];
				H[c][2] = 's';
				H[c][3] = L[s];
				H[c][4] = 0;
			}
	}

	/*
	 * Rating function
	 */
	public long rate(String puzzle) {
		int STATE = M6;
		int Solutions;

		z = 100;
		fi = 0;
		rate = 1;

		for (i = 0; i < 88; i++)
			A[i] = 0;

		initialize();

		while (STATE != END) {
			switch (STATE) {
			case M6:
				clues = 0;
				for (i = 1; i <= 81; i++) {
					c = puzzle.charAt(i - 1);
					j = 0;

					if (c == '-' || c == '.' || c == '0' || c == '*') {
						A[i] = j;
					} else {
						while (L[j] != c && j <= 9)
							j++;

						if (j <= 9) {
							A[i] = j;
						}
					}
				}

				if (clues == 81) {
					clues--;
					A[1] = 0;
				}

				nt = 0;
				mi1 = 9999;
				for (f = 0; f < z; f++) {
					Solutions = solve();
					if (Solutions != 1) {
						if (Solutions > 1)
							nt = -1 * Solutions;
						STATE = END;
						break;
					}
					nt += nodes;
					if (nodes < mi1) {
						mi1 = nodes;
						mi2 = C[clues];
					}
				}
				if (STATE == END)
					break;

				if (fi > 0)
					if ((nt / z) > fi) {
						for (i = 1; i <= 81; i++)
							System.out.println(L[A[i]]);
						System.out.println();
						STATE = M6;
						break;
					}

				if (fi > 0) {
					STATE = M6;
					break;
				}

				if ((z & 1) > 0) {
					System.out.println(nt / z);
					STATE = M6;
					break;
				}

				if (rate > 1)
					System.out.println("hint: " + H[mi2]);

				STATE = END;
				break;
			} // End of switch statement
		} // End of while loop
		return (nt);
	}

	public String[] generate(int Samples, int Rate) {
		int STATE = M0S;
		String result[] = new String[Samples];

		dbg("Entering generate");

		samples = 1000;
		if (Samples > 0)
			samples = Samples;

		for (i = 0; i < samples; i++)
			result[i] = new String();

		// Set to 1 for rating, set to 2 for rating and hint
		rate = 0;
		if (Rate > 0)
			rate = Rate;
		if (rate > 2)
			rate = 2;

		initialize();

		dbg("Entering state machine");

		sam1 = -1;
		while (STATE != END) {
			switch (STATE) {
			case M0S:
				sam1++;
				if (sam1 >= samples) {
					STATE = END;
					break;
				}

			case M0:
				for (i = 1; i <= 81; i++)
					A[i] = 0;
				part = 0;
				q7 = 0;

			case MR1:
				i1 = (int) ((MWC() >> 8) & 127);
				if (i1 > 80) {
					STATE = MR1;
					break;
				}

				i1++;
				if (A[i1] > 0) {
					STATE = MR1;
					break;
				}

			case MR3:
				s = (int) ((MWC() >> 9) & 15);
				if (s > 8) {
					STATE = MR3;
					break;
				}

				s++;
				A[i1] = s;
				m2 = solve();
				q7++;

				if (m2 < 1)
					A[i1] = 0;

				if (m2 != 1) {
					STATE = MR1;
					break;
				}

				part++;
				if (solve() != 1) {
					STATE = M0;
					break;
				}

			case MR4:
				for (i = 1; i <= 81; i++) {
					x = (int) ((MWC() >> 8) & 127);
					while (x >= i) {
						x = (int) ((MWC() >> 8) & 127);
					}
					x++;
					P[i] = P[x];
					P[x] = i;
				}

				for (i1 = 1; i1 <= 81; i1++) {
					s1 = A[P[i1]];
					A[P[i1]] = 0;
					if (solve() > 1)
						A[P[i1]] = s1;
				}

				if (rate > 0) {
					nt = 0;
					mi1 = 9999;
					for (f = 0; f < 100; f++) {
						solve();
						nt += nodes;
						if (nodes < mi1) {
							mi1 = nodes;
							mi2 = C[clues];
						}
					}
					result[sam1] = result[sam1].concat("Rating:" + nt + "# ");
					if (rate > 1) {
						result[sam1] = result[sam1].concat("hint: " + String.valueOf(H[mi2]).substring(0, 4) + " #\n");
					} else
						result[sam1] = result[sam1].concat("\n");
				}

				for (i = 1; i <= 81; i++) {
					result[sam1] = result[sam1].concat(String.valueOf(L[A[i]]));
					if (i % 9 == 0) {
						result[sam1] = result[sam1].concat("\n");
					}
				}
				result[sam1] = result[sam1].concat("\n");

			default:
				dbg("Default case. New state M0S");
				STATE = M0S;
				break;
			} // end of switch statement
		} // end of while loop
		return result;
	}

	int solve() {// returns 0 (no solution), 1 (unique sol.), 2 (more than
					// one sol.)
		int STATE = M2;

		for (i = 0; i <= n; i++)
			Ur[i] = 0;
		for (i = 0; i <= m; i++)
			Uc[i] = 0;
		clues = 0;

		for (i = 1; i <= 81; i++)
			if (A[i] > 0) {
				clues++;
				r = i * 9 - 9 + A[i];

				for (j = 1; j <= Cols[r]; j++) {
					d = Col[r][j];
					if (Uc[d] > 0)
						return 0;
					Uc[d]++;

					for (k = 1; k <= Rows[d]; k++) {
						Ur[Row[d][k]]++;
					}
				}
			}

		for (c = 1; c <= m; c++) {
			V[c] = 0;
			for (r = 1; r <= Rows[c]; r++)
				if (Ur[Row[c][r]] == 0)
					V[c]++;
		}

		i = clues;
		m0 = 0;
		m1 = 0;
		solutions = 0;
		nodes = 0;

		dbg("Solve: Entering state machine");

		while (STATE != END) {
			switch (STATE) {
			case M2:
				i++;
				I[i] = 0;
				min = n + 1;
				if ((i > 81) || (m0 > 0)) {
					STATE = M4;
					break;
				}

				if (m1 > 0) {
					C[i] = m1;
					STATE = M3;
					break;
				}

				w = 0;
				for (c = 1; c <= m; c++)
					if (Uc[c] == 0) {
						if (V[c] < 2) {
							C[i] = c;
							STATE = M3;
							break;
						}

						if (V[c] <= min) {
							w++;
							W[(int) w] = c;
						}
						;

						if (V[c] < min) {
							w = 1;
							W[(int) w] = c;
							min = V[c];
						}
					}

				if (STATE == M3) {
					// break in for loop detected, continue breaking
					break;
				}

			case MR:
				c2 = (MWC() & Two[(int) w]);
				while (c2 >= w) {
					c2 = (MWC() & Two[(int) w]);
				}
				C[i] = W[(int) c2 + 1];

			case M3:
				c = C[i];
				I[i]++;
				if (I[i] > Rows[c]) {
					STATE = M4;
					break;
				}

				r = Row[c][I[i]];
				if (Ur[r] > 0) {
					STATE = M3;
					break;
				}
				m0 = 0;
				m1 = 0;
				nodes++;
				for (j = 1; j <= Cols[r]; j++) {
					c1 = Col[r][j];
					Uc[c1]++;
				}

				for (j = 1; j <= Cols[r]; j++) {
					c1 = Col[r][j];
					for (k = 1; k <= Rows[c1]; k++) {
						r1 = Row[c1][k];
						Ur[r1]++;
						if (Ur[r1] == 1)
							for (l = 1; l <= Cols[r1]; l++) {
								c2 = Col[r1][l];
								V[(int) c2]--;
								if (Uc[(int) c2] + V[(int) c2] < 1)
									m0 = (int) c2;
								if (Uc[(int) c2] == 0 && V[(int) c2] < 2)
									m1 = (int) c2;
							}
					}
				}

				if (i == 81)
					solutions++;

				if (solutions > 1) {
					STATE = M9;
					break;
				}
				STATE = M2;
				break;

			case M4:
				i--;
				if (i == clues) {
					STATE = M9;
					break;
				}
				c = C[i];
				r = Row[c][I[i]];

				for (j = 1; j <= Cols[r]; j++) {
					c1 = Col[r][j];
					Uc[c1]--;
					for (k = 1; k <= Rows[c1]; k++) {
						r1 = Row[c1][k];
						Ur[r1]--;
						if (Ur[r1] == 0)
							for (l = 1; l <= Cols[r1]; l++) {
								c2 = Col[r1][l];
								V[(int) c2]++;
							}
					}
				}

				if (i > clues) {
					STATE = M3;
					break;
				}

			case M9:
				STATE = END;
				break;
			default:
				STATE = END;
				break;
			} // end of switch statement
		} // end of while statement
		return solutions;
	}
}

/**
 * 
 * @author Rolf Sandberg
 */

public class DLXEngine {
	dlx_generator generator;
	dlx_solver solver;

	public DLXEngine(Random random) {
		generator = new dlx_generator(random);
		solver = new dlx_solver(random);
	}

	String generate(int minrating, int maxrating) {
		// Date t = new Date();
		// long start = t.getTime();
		// int tries = 0, i, samples = 5;
		// long rating = 0;
		String ss[] = generator.generate(1, 0);
		return ss[0];

		// Generator:
		// First arg: rand seed
		// Second arg: #samples, ignored if <= 0
		// Third arg: rating and hints, ignored if <= 0

		// Task: Find a Sudoku with a rating in a specified interval.
		// Do it by generating samples and examine them
		// Continue until an appropriate puzzle is found.
		// while(tries < 9999999) {
		// tries++;
		// t = new Date();
		// ss = generator.generate(samples, 0);
		// for(i = 0; i < samples; i++) {
		// rating = generator.rate(ss[i].replace("\n","").trim());
		// if(rating > minrating && rating < maxrating) {
		// return ss[i];
		// }
		// }
		// System.out.println(minrating + ", " + maxrating + ", " + rating + ",
		// looping");
		// }
		// return ss[0];
	}

	long rate(String s) {
		return generator.rate(s);
	}

	String solve(String s) {
		String result = solver.solve(s);
		return result;
	}
}
