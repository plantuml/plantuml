/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program:
 * 
 *************************************************************************
 * Copyright (c) 2011 AT&T Intellectual Property 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: See CVS logs. Details at http://www.graphviz.org/
 *************************************************************************
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package gen.lib.pathplan;
import static smetana.core.JUtils.atan2;
import static smetana.core.JUtils.cos;
import static smetana.core.JUtils.pow;
import static smetana.core.JUtils.sqrt;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;

public class solvers__c {


//3 2rap7a11ad4ugnphkh9gbn4xl
// int solve3(double *coeff, double *roots) 
public static int solve3(double coeff[], double roots[]) {
ENTERING("2rap7a11ad4ugnphkh9gbn4xl","solve3");
try {
    double a, b, c, d;
    int rootn, i;
    double p, q, disc, b_over_3a, c_over_a, d_over_a;
    double r, theta, temp, alpha, beta;
    a = coeff[3]; b = coeff[2]; c = coeff[1]; d = coeff[0];
    if ((((a) < 1E-7) && ((a) > -1E-7)))
	return solve2(coeff, roots);
    b_over_3a = b / (3 * a);
    c_over_a = c / a;
    d_over_a = d / a;
    p = b_over_3a * b_over_3a;
    q = 2 * b_over_3a * p - b_over_3a * c_over_a + d_over_a;
    p = c_over_a / 3 - p;
    disc = q * q + 4 * p * p * p;
    if (disc < 0) {
	r = .5 * sqrt(-disc + q * q);
	theta = atan2(sqrt(-disc), -q);
	temp = 2 * ((r < 0) ? (-1*pow(-r, 1.0/3.0)) : pow (r, 1.0/3.0));
	roots[0] = temp * cos(theta / 3);
	roots[1] = temp * cos((theta + 3.14159265358979323846 + 3.14159265358979323846) / 3);
	roots[2] = temp * cos((theta - 3.14159265358979323846 - 3.14159265358979323846) / 3);
	rootn = 3;
    } else {
	alpha = .5 * (sqrt(disc) - q);
	beta = -q - alpha;
	roots[0] = ((alpha < 0) ? (-1*pow(-alpha, 1.0/3.0)) : pow (alpha, 1.0/3.0)) + ((beta < 0) ? (-1*pow(-beta, 1.0/3.0)) : pow (beta, 1.0/3.0));
	if (disc > 0)
	    rootn = 1;
	else
	    {roots[1] = roots[2] = -.5 * roots[0]; rootn = 3;}
    }
    for (i = 0; i < rootn; i++)
	roots[i] -= b_over_3a;
    return rootn;
} finally {
LEAVING("2rap7a11ad4ugnphkh9gbn4xl","solve3");
}
}




//3 9b5238tdddphds1x726z0osdm
// int solve2(double *coeff, double *roots) 
public static int solve2(double coeff[], double roots[]) {
ENTERING("9b5238tdddphds1x726z0osdm","solve2");
try {
    double a, b, c;
    double disc, b_over_2a, c_over_a;
    a = coeff[2]; b = coeff[1]; c = coeff[0];
    if ((((a) < 1E-7) && ((a) > -1E-7)))
	return solve1(coeff, roots);
    b_over_2a = b / (2 * a);
    c_over_a = c / a;
    disc = b_over_2a * b_over_2a - c_over_a;
    if (disc < 0)
	return 0;
    else if (disc == 0) {
	roots[0] = -b_over_2a;
	return 1;
    } else {
	roots[0] = -b_over_2a + sqrt(disc);
	roots[1] = -2 * b_over_2a - roots[0];
	return 2;
    }
} finally {
LEAVING("9b5238tdddphds1x726z0osdm","solve2");
}
}




//3 8xtqt7j6wyxc1b6tj0qtcu1rd
// int solve1(double *coeff, double *roots) 
public static int solve1(double coeff[], double roots[]) {
ENTERING("8xtqt7j6wyxc1b6tj0qtcu1rd","solve1");
try {
    double a, b;
    a = coeff[1]; b = coeff[0];
    if ((((a) < 1E-7) && ((a) > -1E-7))) {
	if ((((b) < 1E-7) && ((b) > -1E-7)))
	    return 4;
	else
	    return 0;
    }
    roots[0] = -b / a;
    return 1;
} finally {
LEAVING("8xtqt7j6wyxc1b6tj0qtcu1rd","solve1");
}
}


}
