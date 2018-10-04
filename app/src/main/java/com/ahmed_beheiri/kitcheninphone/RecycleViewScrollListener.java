package com.ahmed_beheiri.kitcheninphone;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by ahmed_beheiri on 10/08/17.
 */

public abstract class RecycleViewScrollListener extends RecyclerView.OnScrollListener {
    RecyclerView.LayoutManager LayoutManager;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of Database you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of Database to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    public RecycleViewScrollListener(LinearLayoutManager layoutManager) {
        this.LayoutManager = layoutManager;
    }

    public RecycleViewScrollListener(GridLayoutManager layoutManager) {
        this.LayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public RecycleViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.LayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more Database,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = LayoutManager.getItemCount();

        if (LayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) LayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (LayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) LayoutManager).findLastVisibleItemPosition();
        } else if (LayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) LayoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more Database.
        // If we do need to reload some more Database, we execute onLoadMore to fetch the Database.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    // Call this method whenever performing new searches
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    // Defines the process for actually loading more Database based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
