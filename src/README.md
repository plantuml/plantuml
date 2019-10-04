# PlantUML : a free UML diagram generator

---

(C) Copyright 2009-2019, Arnaud Roques
Project Info: [http://plantuml.com](http://plantuml.com)

This file is part of PlantUML.

PlantUML is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PlantUML distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
License for more details.

You should have received a copy of the GNU General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
USA.

Java is a trademark or registered trademark of Sun Microsystems, Inc.
in the United States and other countries.

Original Author: Arnaud Roques

---


PlantUML is an open-source component used to draw UML diagrams from their textual description.

### Overview
This documentation will _not_ describe the PlantUML language description.
Please refer to [PlantUML site](http://plantuml.com) for that.

Here, some information about how PlantUML is implemented will be provided to help the
integration of PlantUML with other programs.

Unfortunatly, here, we have to raise a **warning**:

While PlantUML language description remains stable over version and follow ascending
compatibility, the *implementation* of PlantUML changes very often over time.

So if you use classes described in this documentation, it's very likely that you will have
an issue someday, because thoses class may change without any notice. They could even be deleted.

It used to happen more often than you think over years, because we try to constantly improve the
general design of PlantUML, and this imply a continuous refactoring.

The only exception is the `net.sourceforge.plantuml` package, that we will keep
as stable as possible over time.
