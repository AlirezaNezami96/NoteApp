package alireza.nezami.designsystem.components

import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.theme.NoteAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
        style: TopBarStyle,
        layoutType: LayoutType = LayoutType.Grid,
        searchQuery: String = "",
        onBack: () -> Unit,
        onSearchClick: () -> Unit = {},
        onSwitchLayout: () -> Unit = {},
        addReminder: () -> Unit = {},
        clearSearch: () -> Unit = {},
        onSearch: (String) -> Unit = {}
) {
    when (style) {
        TopBarStyle.Home -> {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(0.dp)
                    .fillMaxWidth()
                    .padding(4.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Spacer(Modifier.width(8.dp))

                        VerticalDivider(
                            thickness = (0.5).dp,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onSearchClick() })

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.search_your_notes),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground

                        )
                    }
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar),
                        contentDescription = "Avatar",
                        modifier = Modifier.size(40.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = onSwitchLayout,
                        interactionSource = remember { MutableInteractionSource() }) {
                        if (layoutType == LayoutType.Grid) {
                            Icon(
                                painter = painterResource(R.drawable.ic_grid),
                                contentDescription = "Grid",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_row_vertical),
                                contentDescription = "Row",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { },
                        interactionSource = remember { MutableInteractionSource() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_hamb_menu),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                })
        }

        TopBarStyle.Search -> {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(0.dp)
                    .fillMaxWidth()
                    .padding(4.dp),
                title = {
                    TextField(
                        interactionSource = remember { MutableInteractionSource() },
                        value = searchQuery,
                        onValueChange = { onSearch(it) },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_your_notes),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        },
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onBack,
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { clearSearch() },
                        interactionSource = remember { MutableInteractionSource() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                })
        }

        TopBarStyle.Edit -> {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(0.dp)
                    .fillMaxWidth()
                    .padding(4.dp),
                title = { },
                navigationIcon = {
                    IconButton(
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onBack,
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = addReminder,
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_ringtone_add),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(Modifier.width(8.dp))

                    IconButton(
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {},
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_direct_inbox),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarHomePreviewLight() {
    NoteAppTheme(darkTheme = false) {
        HomeTopBar(
            style = TopBarStyle.Home,
            layoutType = LayoutType.Grid,
            searchQuery = "",
            onBack = {},
            onSwitchLayout = {},
            addReminder = {},
            onSearchClick = {},
            clearSearch = {},
            onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarHomePreviewDark() {
    NoteAppTheme(darkTheme = true) {
        HomeTopBar(
            style = TopBarStyle.Home,
            layoutType = LayoutType.Grid,
            searchQuery = "",
            onBack = {},
            onSwitchLayout = {},
            addReminder = {},
            onSearchClick = {},
            clearSearch = {},
            onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarSearchPreviewLight() {
    NoteAppTheme(darkTheme = false) {
        HomeTopBar(
            style = TopBarStyle.Search,
            layoutType = LayoutType.Grid,
            searchQuery = "Search term",
            onBack = {},
            onSwitchLayout = {},
            addReminder = {},
            onSearchClick = {},
            clearSearch = {},
            onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarSearchPreviewDark() {
    NoteAppTheme(darkTheme = true) {
        HomeTopBar(
            style = TopBarStyle.Search,
            layoutType = LayoutType.Grid,
            searchQuery = "Search term",
            onBack = {},
            onSwitchLayout = {},
            addReminder = {},
            onSearchClick = {},
            clearSearch = {},
            onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarEditPreviewLight() {
    NoteAppTheme(darkTheme = false) {
        HomeTopBar(
            style = TopBarStyle.Edit,
            layoutType = LayoutType.Grid,
            searchQuery = "",
            onBack = {},
            onSwitchLayout = {},
            addReminder = {},
            onSearchClick = {},
            clearSearch = {},
            onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarEditPreviewDark() {
    NoteAppTheme(darkTheme = true) {
        HomeTopBar(
            style = TopBarStyle.Edit,
            layoutType = LayoutType.Grid,
            searchQuery = "",
            onBack = {},
            onSwitchLayout = {},
            addReminder = {},
            onSearchClick = {},
            clearSearch = {},
            onSearch = {})
    }
}
