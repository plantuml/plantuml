# Vega tests of Python classes in diagrams

Classes of text Python (section 3.4 of `PYTHON_PREPROCESSOR.md`) used to build a
diagram. The tests avoid `emit`: the text is written as text lines with
`!{expr}`, in `!for` loops, or in the body of a method (a method that contains
text lines emits them when it is called). The expected output is also written
in the `' EXPECT (preproc):` comment of each file.

Groups: instances and attributes (`instances_in_a_loop`,
`default_and_keyword_arguments`, `class_in_a_python_block`), methods
(`method_returns_text`, `method_with_text_lines`, `fluent_builder`,
`bound_method_kept`, `private_attribute`), inheritance and types
(`inheritance_and_override`, `isinstance_dispatch`, `type_and_name`,
`nested_class`, `class_built_in_a_function`, `class_attribute_counter`,
`sorted_by_attribute`), exceptions (`user_exception_caught`,
`exception_hierarchy`), with a macro (`with_a_macro`), the deviation of
`instance_repr_has_no_address`, and errors (`text_line_in_class_body`,
`super_is_rejected`, `uncaught_user_exception`,
`subclassing_list_is_rejected`).
