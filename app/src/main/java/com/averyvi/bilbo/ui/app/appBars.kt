package com.averyvi.bilbo.ui.app

import android.graphics.drawable.shapes.Shape
import android.widget.Toolbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.asampleofuis.ui.theme.FancyHeading
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.InstrumentStyling

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BILBOTopAppBar(
    selectedInstrument: InstrumentStyling,
    scrollBehavior: TopAppBarScrollBehavior,
){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = stringResource(R.string.AppNameShort),
                    fontFamily = FancyHeading.titleLarge.fontFamily,
                    fontWeight = FancyHeading.titleLarge.fontWeight,
                    fontSize = 25.sp,
                    lineHeight = 10.sp,
                    modifier = Modifier.padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 20.dp)
                )
                Text(
                    text = selectedInstrument.instrumentName,
                    fontSize = 20.sp,
                    lineHeight = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 0.dp, bottom = 0.dp, start = 20.dp, end = 0.dp)
                )
            }
                },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun BILBOBottomAppBar(
    selectedInstrument: InstrumentStyling,
    onRouteButtonClicked: (Routes) -> Unit,
){
    BottomAppBar(
        actions = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 0.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(onClick = { onRouteButtonClicked(Routes.InstrumentSelect) } ) {
                        Icon(
                            painter = painterResource(selectedInstrument.instrumentIcon),
                            contentDescription = "Localized description"
                        )
                    }
                    FloatingActionButton(
                        onClick = { onRouteButtonClicked(Routes.CurrentlyPlaying) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            painter = painterResource(selectedInstrument.instrumentIcon),
                            contentDescription = "Localized description"
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BILBOAddInstrumentButton(
    modifier: Modifier = Modifier,
){
    IconButton(
        onClick = {},
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.add_24dp_000000_fill0_wght400_grad0_opsz24),
            modifier = Modifier.fillMaxSize().padding(10.dp),
            contentDescription = null
        )
    }
}