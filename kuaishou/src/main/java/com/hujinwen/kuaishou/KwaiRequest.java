package com.hujinwen.kuaishou;

import okhttp3.FormBody;

public class KwaiRequest {
    public final x0.a0 input;

    public KwaiRequest(x0.a0 input) {
        this.input = input;
    }


    public String getEncodedValue(String encodedName) {
        FormBody formBody = (FormBody) input.a.body;
        if (formBody.encodedNames != null) {
            for (int i = 0; i < formBody.encodedNames.size(); i ++) {
                String encodedNameO = formBody.encodedNames.get(i);
                  if (encodedNameO.equals(encodedName)) {
                      return formBody.encodedValue(i);
                  }
            }
        }
        return null;
    }
}
