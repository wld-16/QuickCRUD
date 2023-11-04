package wld.accelerate.pipelinec.extension

import java.util.Locale
import java.util.regex.Pattern

fun String.Companion.capitalize(fieldName: String) =
    fieldName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.Companion.unCapitalize(fieldName: String) =
    fieldName.replaceFirstChar { if (it.isUpperCase()) it.lowercase(Locale.getDefault()) else it.toString() }

fun String.Companion.snakeCase(fieldName: String) =
    fieldName.split(Pattern.compile("(?=[A-Z])")).joinToString(separator = "_") {
            literal -> literal.lowercase() }