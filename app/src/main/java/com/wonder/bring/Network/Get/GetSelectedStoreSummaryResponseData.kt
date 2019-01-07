package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.StoreSummary

data class GetSelectedStoreSummaryResponseData(
    var status: Int,
    var message: String,
    var data : StoreSummary
)