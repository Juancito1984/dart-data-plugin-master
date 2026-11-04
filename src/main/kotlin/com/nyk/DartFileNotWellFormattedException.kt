package com.nyk

import java.lang.RuntimeException

class DartFileNotWellFormattedException(
    message: String? = null
) : RuntimeException(message)