package com.trang.kidsenglishai.config;

import com.trang.kidsenglishai.BuildConfig;

public class AiConfig {

    private AiConfig() {
    }
    public static final boolean ENABLE_ONLINE_AI =
            BuildConfig.ENABLE_ONLINE_AI;
    public static final String GEMINI_API_KEY =
            BuildConfig.GEMINI_API_KEY;
    public static final String GEMINI_MODEL =
            BuildConfig.GEMINI_MODEL;
    public static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/";
    public static boolean isConfigured() {
        return ENABLE_ONLINE_AI
                && GEMINI_API_KEY != null
                && !GEMINI_API_KEY.trim().isEmpty();
    }
    public static String buildGenerateContentUrl() {

        return BASE_URL
                + GEMINI_MODEL
                + ":generateContent?key="
                + GEMINI_API_KEY;
    }
}