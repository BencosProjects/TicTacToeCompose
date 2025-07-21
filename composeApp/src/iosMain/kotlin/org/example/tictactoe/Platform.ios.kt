package org.example.tictactoe

import androidx.compose.runtime.Composable
import platform.UIKit.UIDevice
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyle
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyle
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun isAndroid(): Boolean = false

actual fun isDesktop(): Boolean = false

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WinnerDialog(winner: String, onDismiss: () -> Unit) {

    val titleText: String = if (winner.isNotEmpty()) "המשחק נגמר!" else "זה תיקו!"
    val messageText: String = if (winner.isNotEmpty()) "המנצח הוא: $winner" else "אין מנצח!"

    // קריאה לקוד של iOS צריכה להתבצע ב-Main Thread.
    dispatch_async(dispatch_get_main_queue()) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = titleText,
            message = messageText,
            preferredStyle = UIAlertControllerStyle.UIAlertControllerStyleAlert
        )

        val closeAction = UIAlertAction.actionWithTitle(
            title = "סגור",
            style = UIAlertActionStyle.UIAlertActionStyleDefault,
            handler = {
                onDismiss() // קריאה חזרה לפונקציה של Compose
                Unit // ב-Kotlin/Native, handler חייב להחזיר Unit
            }
        )

        alert.addAction(closeAction)

        val rootViewController = getRootViewController()
        rootViewController?.presentViewController(alert, animated = true, completion = null)
    }
}

// פונקציית עזר למציאת ה-Root View Controller הנוכחי
@OptIn(ExperimentalForeignApi::class)
private fun getRootViewController(): UIViewController? {
    val window = UIApplication.sharedApplication.keyWindow
    return window?.rootViewController
}