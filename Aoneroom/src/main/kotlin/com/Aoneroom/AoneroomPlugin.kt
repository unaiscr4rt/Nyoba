package com.Aoneroom

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class AoneroomPlugin : Plugin() {
    override fun load() {
        registerMainAPI(AoneroomProvider())
    }
}
