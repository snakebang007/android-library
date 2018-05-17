/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2016 ownCloud GmbH.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package com.owncloud.android.lib.refactor.authentication.credentials;

import android.net.Uri;

import com.owncloud.android.lib.refactor.authentication.OwnCloudCredentials;

import org.apache.commons.httpclient.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnCloudSamlSsoCredentials implements OwnCloudCredentials {

    private final String mUsername;
    private final String mSessionCookie;
    private final Uri mBaseUrl;

    public OwnCloudSamlSsoCredentials(String username, String sessionCookie, Uri baseUrl) {
        mUsername = username != null ? username : "";
        mSessionCookie = sessionCookie != null ? sessionCookie : "";
        mBaseUrl = baseUrl;
    }

    @Override
    public String getCredentialCookie() {

        String[] rawCookies = mSessionCookie.split(";");
        StringBuilder processedCookies = new StringBuilder();
        Cookie cookie = null;
        for (final String rawCookie : rawCookies) {
            int equalPos = rawCookie.indexOf('=');
            if (equalPos >= 0) {
                cookie = new Cookie();
                cookie.setName(rawCookie.substring(0, equalPos));
                cookie.setValue(rawCookie.substring(equalPos + 1));
                cookie.setDomain(mBaseUrl.getHost());    // VERY IMPORTANT
                cookie.setPath(mBaseUrl.getPath());    // VERY IMPORTANT
                processedCookies.append(cookie.toExternalForm() + ";");
            }
        }
        return processedCookies.toString();
    }

    @Override
    public Map<String, String> getCredentialHeaders() {
        return new HashMap<>(0);
    }

    @Override
    public String getUsername() {
        // not relevant for authentication, but relevant for informational purposes
        return mUsername;
    }

    @Override
    public String getAuthToken() {
        return mSessionCookie;
    }

    @Override
    public boolean authTokenCanBeRefreshed() {
        return false;
    }

}