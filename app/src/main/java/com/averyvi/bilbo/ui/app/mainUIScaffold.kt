package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.definitions.BottomButton
import com.averyvi.bilbo.data.storage.InstrumentDBRow
import com.averyvi.bilbo.data.storage.UserDao
import com.averyvi.bilbo.definitions.AppBarsVisibility
import com.averyvi.bilbo.data.uiState.TuningViewModel
import kotlin.concurrent.thread
import androidx.compose.runtime.collectAsState
import com.averyvi.bilbo.data.uiState.InstrumentProfileViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold(
    modifier: Modifier = Modifier,
    instrumentProfileViewModel: InstrumentProfileViewModel,
    tuningViewModel: TuningViewModel,
    dbDao: UserDao,
    bottomButton: BottomButton,
    barsVisibility: AppBarsVisibility,
    scrollEnabled: Boolean = true,
    onRouteButtonClicked: (Boolean) -> Unit,
    content: @Composable ((PaddingValues) -> Unit),
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { scrollEnabled }
    )

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if((barsVisibility == AppBarsVisibility.top) || (barsVisibility == AppBarsVisibility.both)) {
                BILBOTopAppBar(
                    selectedInstrumentName = instrumentProfileViewModel.currentInstrument.collectAsState().value.name,
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
        ) {
            content(innerPadding)
            if((barsVisibility == AppBarsVisibility.bottom) || (barsVisibility == AppBarsVisibility.both)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 15.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (bottomButton.name == "new") BILBONewInstrumentButton(
                            onRouteButtonClicked = onRouteButtonClicked
                        )
                        else if(bottomButton.name == "add") BILBOAddInstrumentButton(
                            onClick = {
                                if( instrumentProfileViewModel.newInstrument.value.freq.isEmpty()) instrumentProfileViewModel.updateNewFreq("0")
                                thread {
                                    dbDao.insertAll(
                                        instrument = InstrumentDBRow(
                                            instrumentName = instrumentProfileViewModel.newInstrument.value.name,
                                            instrumentIcon = instrumentProfileViewModel.newInstrument.value.icon, //todo add the possibility of adding an icon
                                            refFreq = instrumentProfileViewModel.newInstrument.value.freq.takeLast(9).toInt(),
                                            positionInOctive = instrumentProfileViewModel.newInstrument.value.note.ordinal,
                                            refOctive = instrumentProfileViewModel.newInstrument.value.octive
                                        )
                                    )
                                }.join()
                                //reset values
                                instrumentProfileViewModel.resetNewValues()
                                //navigate
                                onRouteButtonClicked(true)
                            }
                        )
                        BILBONavPill(
                            tuningViewModel = tuningViewModel,
                            onRouteButtonClicked = onRouteButtonClicked
                        )
                    }
                }
            }
        }

    }
}