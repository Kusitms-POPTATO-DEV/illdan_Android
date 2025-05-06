package com.poptato.setting.servicedelete.finish

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.DeleteFinishContent
import com.poptato.design_system.DeleteFinishTitle
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray30
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ServiceDeleteFinishScreen(
    deleteUserName: SharedFlow<String>,
    goBackToLogIn: () -> Unit = {},
) {
    val viewModel: ServiceDeleteFinishViewModel = hiltViewModel()
    val uiState: ServiceDeleteFinishPageState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(deleteUserName) {
        deleteUserName.collect {
            viewModel.getDeleteUserName(it)
        }
    }

    LaunchedEffect(Unit) {
        delay(2000L)
        goBackToLogIn()
    }

    ServiceDeleteFinishContent(
        deleteUserName = uiState.userName
    )
}

@Composable
fun ServiceDeleteFinishContent(
    deleteUserName: String = ""
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_withdrawal),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format(DeleteFinishTitle, deleteUserName),
                color = Gray00,
                style = PoptatoTypo.xxxLSemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = DeleteFinishContent,
                color = Gray30,
                style = PoptatoTypo.lgMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewServiceDeleteFinish() {
    ServiceDeleteFinishContent()
}