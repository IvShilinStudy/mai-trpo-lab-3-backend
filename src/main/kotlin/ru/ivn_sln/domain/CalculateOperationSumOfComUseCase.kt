package ru.ivn_sln.domain

import org.koin.core.component.KoinComponent
import ru.ivn_sln.data.response.OperationExtendedResponse
import kotlin.math.roundToInt

class CalculateOperationSumOfComUseCase : KoinComponent {
    operator fun invoke(operationExtended: OperationExtendedResponse): Int {
        val finallyCoeff = operationExtended.type.coeff * operationExtended.category.coeff
        val sumOfCom = (operationExtended.sum * finallyCoeff).roundToInt()

        return sumOfCom
    }
}