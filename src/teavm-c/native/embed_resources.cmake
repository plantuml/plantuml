# plantuml_embed_resources(<output> <name>=<file> ...)
#
# Embeds the bytes of every <file> in the executable and registers them, under
# the logical <name> that Java asks for (for instance
# "themes/puml-theme-amiga.puml"), in the table that plantuml_resources.c
# searches: <output> is the C file that defines
#   const plantuml_resource plantuml_resources[];
#   const int32_t plantuml_resources_count;
# It is generated at build time by native/embed_tool.c, and again whenever a
# <file> changes.
# Portable replacement for xxd -i or C23 #embed (MSVC supports neither).
function(plantuml_embed_resources output)
	if (NOT TARGET plantuml_embed_tool)
		add_executable(plantuml_embed_tool ${PROJECT_SOURCE_DIR}/native/embed_tool.c)
	endif()
	set(list_file ${output}.list)
	set(content "")
	set(files "")
	foreach(pair IN LISTS ARGN)
		string(APPEND content "${pair}\n")
		string(FIND "${pair}" "=" equals)
		math(EXPR path_start "${equals} + 1")
		string(SUBSTRING "${pair}" ${path_start} -1 path)
		list(APPEND files ${path})
	endforeach()
	# (A list file, not arguments: a command line has a length limit)
	file(WRITE ${list_file} "${content}")
	add_custom_command(OUTPUT ${output}
		COMMAND plantuml_embed_tool ${output} ${list_file}
		DEPENDS plantuml_embed_tool ${list_file} ${files}
		COMMENT "Embedding resources")
endfunction()
