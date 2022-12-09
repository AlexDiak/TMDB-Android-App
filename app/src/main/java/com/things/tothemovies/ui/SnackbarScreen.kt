package com.things.tothemovies.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SnackbarScreen(snackbarHostState: SnackbarHostState, text: String) {
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(text, duration = SnackbarDuration.Short)
    }
}