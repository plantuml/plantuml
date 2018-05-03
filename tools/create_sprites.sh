#!/usr/bin/env bash

# 
# (C) Copyright 2017, Anthony Gaudino
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# 


#
# Batch creates sprite files for PlantUml
#
# Given a directory can convert all SVG files to PNG, then on the same directory,
# convert all PNG files to PlantUml sprite files.
# To convert SVGs the -s flag must be set.
#
# The generated sprites files formats are based on the ones introduced in
# PlantUml 1.2017.19.
#
# This script assumes InkScape and PlantUml are in PATH.
#


# Help usage message
usage="Batch creates sprite files for PlantUml.

$(basename "$0") [options] prefix

options:
    -p  directory path to process  Default: ./
    -w  width  of PNG from SVG     Default: 48
    -h  height of PNG from SVG     Default: 48
    -g  sprite graylevel           Default: 16
    -s  if set processes SVGs
    
    prefix: a prefix that is added to the sprite name"



# Default arguments values
        dir="./"  # Directory path to process     Default: ./
      width=48    # Width  of PNG to generate     Default: 48
     height=48    # Height of PNG to generate     Default: 48
  graylevel=16    # Number of grayscale colors    Default: 16
 processsvg=0     # 1 if SVGs should be processed Default: 0

     prefix=""    # Prefix for sprites names, avoids having
prefixupper=""    # two sprites with same name on STDLIB



########################################
#
#    Main function
#
########################################
main () {
    # Get arguments
    while getopts p:w:h:g:s option
    do
        case "$option" in
            p)        dir="$OPTARG";;
            w)      width="$OPTARG";;
            h)     height="$OPTARG";;
            g)  graylevel="$OPTARG";;
            s) processsvg=1;;
            :) echo "$usage"
               exit 1
               ;;
           \?) echo "$usage"
               exit 1
               ;;
        esac
    done

    # Get mandatory argument
    shift $(($OPTIND-1))
    prefix=$(     echo $1 | tr '[:upper:]' '[:lower:]')
    prefixupper=$(echo $1 | tr '[:lower:]' '[:upper:]')

    # Check mandatory argument
    if [ -z "$prefix" ]
    then
        echo "Please specify a prefix!"
        echo "$usage"
        exit 1
    fi



    # Change dir to where images are
    if [ ! -d "${dir}" ]
    then
        echo "Please specify a valid directory"
        echo "$usage"
        exit 1
    fi
    
    cd "$dir"



    # Processes dir
    if [ "$processsvg" -eq 1 ]
    then
        process_svg
    fi
    
    process_png
}


########################################
#
#    Convert all SVG files in directory to PNG
#
########################################
process_svg () {
    for i in *.svg
    do
        [ -f "$i" ] || continue

        inkscape -z --export-png=`echo $i | sed -e 's/svg$/png/' | sed 's/[^a-zA-Z0-9._]/_/g'` -w $width -h $height -b white $i
    done
}



########################################
#
#    Generate PlantUml sprite
#
########################################
process_png () {
    for i in *.png
    do
        [ -f "$i" ] || continue

               filename=$(echo $i | sed -e 's/.png$//')                 # Filename without extension
          filenameupper=$(echo $filename | tr '[:lower:]' '[:upper:]')  # Filename without extension in uppercase
             spritename="${prefix}_$filename"                           # Sprite name is composed by prefix_filename
        spritenameupper="${prefixupper}_$filenameupper"                 # Sprite name in uppercase
           spritestereo="$prefixupper $filenameupper"                   # Sprite stereotype is uppercase prefix followed by uppercase filename
           stereowhites=$(echo $spritestereo | sed -e 's/./ /g')        # This is just whitespace to make output nicer

        #echo "@startuml" >> $filename.puml

        echo -e "$(plantuml -encodesprite $graylevel $i | sed '1!b;s/\$/$'${prefix}_'/')\n" >> $filename.puml

        echo "!define $spritenameupper(_color)                                 SPRITE_PUT(          $stereowhites          $spritename, _color)"                 >> $filename.puml
        echo "!define $spritenameupper(_color, _scale)                         SPRITE_PUT(          $stereowhites          $spritename, _color, _scale)"         >> $filename.puml

        echo "!define $spritenameupper(_color, _scale, _alias)                 SPRITE_ENT(  _alias, $spritestereo,         $spritename, _color, _scale)"         >> $filename.puml
        echo "!define $spritenameupper(_color, _scale, _alias, _shape)         SPRITE_ENT(  _alias, $spritestereo,         $spritename, _color, _scale, _shape)" >> $filename.puml
        echo "!define $spritenameupper(_color, _scale, _alias, _shape, _label) SPRITE_ENT_L(_alias, $spritestereo, _label, $spritename, _color, _scale, _shape)" >> $filename.puml
        
        echo "skinparam folderBackgroundColor<<$prefixupper $filenameupper>> White"                                                                              >> $filename.puml
        
        #echo "@enduml" >> $filename.puml
    done
}


main "$@"
