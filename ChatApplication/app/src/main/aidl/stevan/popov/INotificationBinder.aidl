// INotificationBinder.aidl
package stevan.popov;

// Declare any non-default types here with import statements

import stevan.popov.INotificationCallback;

interface INotificationBinder {
    void setCallback(in INotificationCallback callback);
}
