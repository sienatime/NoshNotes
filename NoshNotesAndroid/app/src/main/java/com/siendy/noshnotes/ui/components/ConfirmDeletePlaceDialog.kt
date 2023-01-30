package com.siendy.noshnotes.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.main.MainViewModel

@Composable
fun ConfirmDeletePlaceDialog(
  navHostController: NavHostController,
  mainViewModel: MainViewModel,
  placeId: String?
) {
  Dialog(
    {
      mainViewModel.deletePlace(placeId)
    },
    navHostController,
    R.string.confirm
  ) {
    Text(
      text = stringResource(id = R.string.confirm_delete_place)
    )
  }
}
