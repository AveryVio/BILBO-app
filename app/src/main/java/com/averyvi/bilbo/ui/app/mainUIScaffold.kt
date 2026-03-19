package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.InstrumentStyling
import com.averyvi.bilbo.ui.fragments.BILBONavPill

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold(
    modifier: Modifier = Modifier,
    selectedInstrumentStyling: InstrumentStyling,
    note: State<String>,
    octive: State<String>,
    pitch: MutableState<Int>,
    showNewInstrumentButton: Boolean,
    showTopBar: Boolean,
    showBottomBar: Boolean,
    scrollEnabled: Boolean = true,
    onRouteButtonClicked: () -> Unit,
    content: @Composable ((PaddingValues) -> Unit),
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { scrollEnabled }
    )

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if(showTopBar) {
                BILBOTopAppBar(
                    selectedInstrument = selectedInstrumentStyling,
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        bottomBar = {

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,

        ) {
            content(innerPadding)
            if(showBottomBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BILBONavPill(
                        note = note,
                        octive = octive,
                        pitch = pitch,
                        onRouteButtonClicked = onRouteButtonClicked
                    )
                    if (showNewInstrumentButton) BILBOAddInstrumentButton()
                }
            }
        }

    }
}