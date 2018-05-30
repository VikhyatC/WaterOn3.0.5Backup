package com.wateron.smartrhomes.util;

import com.wateron.smartrhomes.models.Account;

import java.util.List;

/**
 * Created by Paranjay on 15-12-2017.
 */

public interface AccountHandlerInterface {

    void errorLoadingMembers(String response, int httpResult, String url, String xmsin,String token);

    void loadData(List<String> numbers);
    void loadData();

    void loadData(Account member_number);

    void loadAddedData(String number);

    void errorLoadingDeletedMembers(String response, int httpResult, String s, String s1, String s2, int member_ccode, String member_mobile, long apt_id, String string);
}
