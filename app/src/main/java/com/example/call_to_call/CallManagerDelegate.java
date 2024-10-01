package com.example.call_to_call;

public interface CallManagerDelegate {
    void didUpdateCallStartTime(long startTime);
    void didUpdateCallEndTime(long endTime);
    void didUpdateCallDuration(long duration);
}
