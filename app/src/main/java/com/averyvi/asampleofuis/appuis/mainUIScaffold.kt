package com.averyvi.bilbo.appuis

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.node.Ref
import com.averyvi.bilbo.notuis.InstrumentStyling

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold(
    selectedInstrumentStyling: InstrumentStyling,
    modifier: Modifier = Modifier,
    scrollEnabled: Boolean = true,
    onHelpButtonClicked: () -> Unit,
    onInstrumentButtonClicked: () -> Unit,
    onCurrentlyPlayingClicked: () -> Unit,
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
                onHelpButtonClicked = onHelpButtonClicked,
            )
        },
        bottomBar = {
            BILBOBottomAppBar(
                selectedInstrument = selectedInstrumentStyling,
                onInstrumentButtonClicked = onInstrumentButtonClicked,
                onCurrentlyPlayingClicked = onCurrentlyPlayingClicked,
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}