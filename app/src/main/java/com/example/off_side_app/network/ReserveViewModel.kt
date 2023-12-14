package com.example.off_side_app.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.network.ReserveConnectionApi.Companion.reserveApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class ReserveViewModel : ViewModel() {

    // MutableLiveData로 변경
    private val _matchingQData = MutableLiveData<List<String>>()
    val matchingQData: LiveData<List<String>> get() = _matchingQData

    private val _reservationListData = MutableLiveData<List<String>>()
    val reservationListData: LiveData<List<String>> get() = _reservationListData

    private val _refereeTimeData = MutableLiveData<List<String>>()
    val refereeTimeData: LiveData<List<String>> get() = _refereeTimeData

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _userphone = MutableLiveData<String>()
    val userphone: LiveData<String> get() = _userphone

    private val _matchingname = MutableLiveData<String>()
    val matchingname: LiveData<String> get() = _matchingname

    private val _matchingphone = MutableLiveData<String>()
    val matchingphone: LiveData<String> get() = _matchingphone

    private val _matchinginfo = MutableLiveData<String>()
    val matchinginfo: LiveData<String> get() = _matchinginfo


    suspend fun getReserveData1(stadiumId: Int, date: String): ReserveResponse? {
        return try {
            ReserveConnectionApi.reserveApi.getReserveInfo1(stadiumId, date)
        } catch (e: Exception) {
            // 네트워크 호출 중 예외가 발생한 경우 처리
            // 예를 들어, 로깅 또는 사용자에게 알림 표시 등의 작업 추가
            null // 예외 발생 시 null을 반환하거나 다른 적절한 방식으로 처리
        }
    }

    suspend fun getMathingData(stadiumId: Int, date: String, time: String): MatchingConfirmRequest? {
        return try {
            reserveApi.successMatching(stadiumId, date, time)
        } catch (e: Exception) {
            // 네트워크 호출 중 예외가 발생한 경우 처리
            // 예를 들어, 로깅 또는 사용자에게 알림 표시 등의 작업 추가
            null // 예외 발생 시 null을 반환하거나 다른 적절한 방식으로 처리
        }
    }

    suspend fun getReserveData2(stadiumId: Int, date: String, time: String): ReservationRequest2? {
        return try {
            reserveApi.getReserveInfo2(stadiumId, date, time)
        } catch (e: Exception) {
            // 네트워크 호출 중 예외가 발생한 경우 처리
            // 예를 들어, 로깅 또는 사용자에게 알림 표시 등의 작업 추가
            Log.e("NetworkError", "Error fetching data", e)
            null // 예외 발생 시 null을 반환하거나 다른 적절한 방식으로 처리
        }
    }

    suspend fun getRefereeData(refereeId: Int, date: String): RefereeRequest2?{
        return try {
            reserveApi.getRefereeInfo(refereeId, date)
        } catch (e: Exception) {
            // 네트워크 호출 중 예외가 발생한 경우 처리
            // 예를 들어, 로깅 또는 사용자에게 알림 표시 등의 작업 추가
            Log.e("NetworkError", "Error fetching data", e)
            null // 예외 발생 시 null을 반환하거나 다른 적절한 방식으로 처리
        }
    }

    // LiveData에 값을 설정하는 함수
    private fun setReserveData(reserveResponse: ReserveResponse?) {
        // ReserveResponse가 null이면 빈 리스트를 설정
        val matchingQ = reserveResponse?.matchingQ.orEmpty()
        val reservationList = reserveResponse?.reservationList.orEmpty()

        _matchingQData.value = matchingQ
        _reservationListData.value = reservationList
    }

    private fun setReserveData2(reservationRequest2: ReservationRequest2?) {
        // ReserveResponse가 null이면 빈 리스트를 설정
        val userName = reservationRequest2?.userName.orEmpty()
        val userPhone = reservationRequest2?.userPhone.orEmpty()

        _username.value = userName
        _userphone.value = userPhone
    }

    private fun setMatchingData(matchingConfirmRequest: MatchingConfirmRequest?) {
        // ReserveResponse가 null이면 빈 리스트를 설정
        val matchingname = matchingConfirmRequest?.userName.orEmpty()
        val matchingphone = matchingConfirmRequest?.contactPhone.orEmpty()
        val matchinginfo = matchingConfirmRequest?.comment.orEmpty()

        _matchingname.value = matchingname
        _matchingphone.value = matchingphone
        _matchinginfo.value = matchinginfo
    }

    private fun setRefreeData(refreeRequest2: RefereeRequest2?) {
        // ReserveResponse가 null이면 빈 리스트를 설정
        val availabletime = refreeRequest2?.availableTime.orEmpty()

        _refereeTimeData.value = availabletime
    }

    // LiveData를 사용하여 데이터를 변경하도록 수정
    fun fetchDataAndChangeButtonBackground1(
        stadiumId: Int,
        date: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val reserveData = getReserveData1(stadiumId, date)
                setReserveData(reserveData)

                // 성공 콜백 호출
                onSuccess()
            } catch (e: Exception) {
                // 네트워크 호출 중 예외 발생 시 처리
                onError(e)
            }
        }
    }

    fun fetchDataNameAndPnum(
        stadiumId: Int,
        date: String,
        time:String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val reserveData2 = getReserveData2(stadiumId, date, time)
                setReserveData2(reserveData2)

                // 성공 콜백 호출
                onSuccess()
            } catch (e: Exception) {
                // 네트워크 호출 중 예외 발생 시 처리
                onError(e)
            }
        }
    }

    fun fetchcommentData(
        stadiumId: Int,
        date: String,
        time:String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val matchingData = getMathingData(stadiumId, date, time)
                setMatchingData(matchingData)

                // 성공 콜백 호출
                onSuccess()
            } catch (e: Exception) {
                // 네트워크 호출 중 예외 발생 시 처리
                onError(e)
            }
        }
    }

    fun fetchDataAndChangeButtonBackground_r(
        refereeId: Int,
        date: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val refereedata = getRefereeData(refereeId, date)
                setRefreeData(refereedata)

                // 성공 콜백 호출
                onSuccess()
            } catch (e: Exception) {
                // 네트워크 호출 중 예외 발생 시 처리
                onError(e)
            }
        }
    }
}



