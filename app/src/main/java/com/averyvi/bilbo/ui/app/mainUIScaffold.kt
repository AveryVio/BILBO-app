package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.BottomButton
import com.averyvi.bilbo.definitions.InstrumentStyling
import com.averyvi.bilbo.definitions.MusicalNote
import com.averyvi.bilbo.storage.InstrumentDBRow
import com.averyvi.bilbo.storage.UserDao
import com.averyvi.bilbo.ui.fragments.InstrumentViewModel
import com.averyvi.bilbo.ui.fragments.TuningViewModel
import kotlin.concurrent.thread

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold(
    modifier: Modifier = Modifier,
    instrumentViewModel: InstrumentViewModel,
    tuningViewModel: TuningViewModel,
    dbDao: UserDao,
    selectedInstrumentStyling: InstrumentStyling,
    bottomButton: BottomButton,
    showTopBar: Boolean,
    showBottomBar: Boolean,
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
            if(showTopBar) {
                BILBOTopAppBar(
                    selectedInstrument = selectedInstrumentStyling,
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
            if(showBottomBar) {
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
                                if(instrumentViewModel.freq.value.isEmpty()) instrumentViewModel.updateFreq("0")
                                thread {
                                    dbDao.insertAll(
                                        instrument = InstrumentDBRow(
                                            instrumentName = instrumentViewModel.name.value,
                                            instrumentIcon = R.drawable.radio_button_checked_24px, //todo add the possibility of adding an icon
                                            refFreq = instrumentViewModel.freq.value.takeLast(9).toInt(),
                                            positionInOctive = instrumentViewModel.note.value.noteNumber,
                                            refOctive = instrumentViewModel.octive.value
                                        )
                                    )
                                }.join()
                                //reset values
                                instrumentViewModel.resetValues()
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