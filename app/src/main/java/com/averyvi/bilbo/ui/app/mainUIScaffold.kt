package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.InstrumentStyling

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold(
    selectedInstrumentStyling: InstrumentStyling,
    showNewInstrumentButton: Boolean,
    modifier: Modifier = Modifier,
    scrollEnabled: Boolean = true,
    onRouteButtonClicked: (Routes) -> Unit,
    content: @Composable ((PaddingValues) -> Unit),
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { scrollEnabled }
    )

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BILBOTopAppBar(
                selectedInstrument = selectedInstrumentStyling,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = { //todo: do this differently than a floating action button
            BILBOAddInstrumentButton()
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}