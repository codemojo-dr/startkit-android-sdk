package io.codemojo.sdk.models;

import java.util.List;

/**
 * Created by shoaib on 26/10/14.
 */
public class PaginatedTransaction<T> {

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private String prev_page_url;
    private int from;
    private int to;
    private List<T> data;

    public int getTotal() {
        return total;
    }

    public int getItemsPerPage() {
        return per_page;
    }

    public int getCurrentPage() {
        return current_page;
    }

    public int getLastPage() {
        return last_page;
    }

    public String getNextPageUrl() {
        return next_page_url;
    }

    public String getPrevPageUrl() {
        return prev_page_url;
    }

    public int getCurrentCollectionStart() {
        return from;
    }

    public int getCurrentCollectionEnd() {
        return to;
    }

    public List<T> getData() {
        return data;
    }
}
