package com.nyk.action

import com.nyk.action.data.GenerationData
import com.nyk.action.data.PerformAction

interface StaticActionProcessor {

    fun processAction(generationData: GenerationData): PerformAction?

}
