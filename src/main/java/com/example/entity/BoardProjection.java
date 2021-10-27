package com.example.entity;

import java.util.Date;

public interface BoardProjection {
    long getNo(); // no

    int getHit(); // hit

    String getTitle(); // title

    String getContent(); // content

    String getWriter(); // writer

    Date getRegdate(); // regdate

}
