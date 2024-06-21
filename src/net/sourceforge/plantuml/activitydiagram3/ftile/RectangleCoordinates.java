/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Timo Schoemaker
 * 
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;
public class RectangleCoordinates{
    private double x1;

    public double getX1(){
        return x1;
    }

    public void setX1(double x1){
        this.x1=x1;
    }

    private double y1;

    public double getY1(){
        return y1;
    }

    public void setY1(double y1){
        this.y1=y1;
    }

    private double x2;

    public double getX2(){
        return x2;
    }

    public void setX2(double x2){
        this.x2=x2;
    }

    private double y2;

    public double getY2(){
        return y2;
    }

    public void setY2(double y2){
        this.y2=y2;
    }

    public RectangleCoordinates(double x1,double y1,double x2,double y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
}

