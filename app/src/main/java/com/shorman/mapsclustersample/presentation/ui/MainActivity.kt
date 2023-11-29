package com.shorman.mapsclustersample.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.clustering.view.DefaultAdvancedMarkersClusterRenderer
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.shorman.mapsclustersample.domain.model.MarkerItemModel
import com.shorman.mapsclustersample.presentation.theme.MapsClusterSampleTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var width: Int? = null
    private var height: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels
        height = metrics.heightPixels

        setContent {
            MapsClusterSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val loadingState by viewModel.loading.collectAsState(initial = true)
                    val errorState by viewModel.error.collectAsState(initial = null)
                    val clusteringData by viewModel.clusteringData.collectAsState()

                    if (loadingState) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Getting clusters please wait....")
                            }

                        }
                    } else {
                        if (errorState != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "Error: ${errorState!!.message}",
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            clusteringData?.let {
                                GoogleMapClusteringWrapper(
                                    markers = it.subList(0, 100),
                                    context = this@MainActivity,
                                    screenWidth = width ?: 0,
                                    screenHeight = height ?: 0
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleMapClusteringWrapper(
    markers: List<MarkerItemModel>,
    context: Context,
    screenWidth: Int,
    screenHeight: Int,
) {
    val items = markers.map {
        MyItem(
            itemPosition = LatLng(
                it.coordinates?.latitude ?: 0.0,
                it.coordinates?.longitude ?: 0.0
            ),
            itemTitle = it.name ?: "",
            itemSnippet = it.shortDescription ?: "",
            itemZIndex = 0f,
            image = it.bannerImages?.let { images ->
                return@let if(images.isNotEmpty()) {
                    images[0]
                } else {
                    ""
                }
            } ?: ""

        )
    }
    GoogleMapClustering(
        items = items,
        context = context,
        screenWidth = screenWidth,
        screenHeight = screenHeight,
    )
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapClustering(
    items: List<MyItem>,
    context: Context,
    screenWidth: Int,
    screenHeight: Int,
) {

    val clusterManager = remember { mutableStateOf<ClusterManager<MyItem>?>(null) }
    val clusterRender = remember { mutableStateOf<ClusterRenderer<MyItem>?>(null) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(items.first().position, 5f)
        }
    ) {
        MapEffect(Unit) {
            clusterManager.value = ClusterManager<MyItem>(context, it)
            clusterManager.value?.setAlgorithm(
                NonHierarchicalViewBasedAlgorithm(
                    screenWidth, screenHeight
                )
            )
            clusterManager.value?.setAnimation(false)

            clusterManager.value?.clearItems()

            clusterRender.value = DefaultAdvancedMarkersClusterRenderer(
                context, it, clusterManager.value
            )

            clusterRender.value?.setAnimation(false)
        }

        Clustering(
            items = items,
            clusterContent = { cluster ->
                Surface(
                    Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.Blue,
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "%,d".format(cluster.size),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            clusterItemContent = {
                Surface(
                    Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.Blue,
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    val request = ImageRequest.Builder(LocalContext.current).data(it.image).allowHardware(false).build()
                    AsyncImage(
                        model = request,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            },
            clusterRenderer = clusterRender.value
        )
    }

}


data class MyItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemZIndex: Float,
    val image: String,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet

    override fun getZIndex(): Float =
        itemZIndex
}