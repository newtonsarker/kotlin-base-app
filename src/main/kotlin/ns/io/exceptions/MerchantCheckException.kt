package ns.io.exceptions

class MerchantCheckException : Exception {

    val errorCode: String
    val errorMessage: String

    constructor(errorCode: String, errorMessage: String, exceptionMessage: String) : super(exceptionMessage) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

}
