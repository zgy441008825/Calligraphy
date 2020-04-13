package com.zougy.calligraphy.dao;

import org.xutils.db.annotation.Column;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description:字帖<br>
 * Author:邹高原<br>
 * Date:04/10 0010<br>
 * Email:441008824@qq.com
 */
public class CopybookBean implements Serializable {

    @Column(name = ConsValue.DB.TAB_COL_NAME_ID, isId = true)
    private int id;

    /**
     * 名称
     */
    @Column(name = ConsValue.DB.TAB_BOOK_NAME)
    private String bookName;

    /**
     * 作者
     */
    @Column(name = ConsValue.DB.TAB_BOOK_AUTHOR)
    private String author;

    /**
     * 年份
     */
    @Column(name = ConsValue.DB.TAB_BOOK_YEAR)
    private String year;

    /**
     * 内容
     */
    @Column(name = ConsValue.DB.TAB_BOOK_CONTENT)
    private String content;

    /**
     * 词条类型
     *
     * @see CopybookType
     */
    @Column(name = ConsValue.DB.TAB_BOOK_TYPE)
    private int type;

    public int getId() {
        return id;
    }

    public CopybookBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getBookName() {
        return bookName;
    }

    public CopybookBean setBookName(String bookName) {
        this.bookName = bookName;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CopybookBean setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getYear() {
        return year;
    }

    public CopybookBean setYear(String year) {
        this.year = year;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CopybookBean setContent(String content) {
        this.content = content;
        return this;
    }

    public int getType() {
        return type;
    }

    public CopybookBean setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CopybookBean that = (CopybookBean) o;
        return type == that.type &&
                Objects.equals(bookName, that.bookName) &&
                Objects.equals(author, that.author) &&
                Objects.equals(year, that.year) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookName, author, year, content, type);
    }

    @Override
    public String toString() {
        return "CopybookBean{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
