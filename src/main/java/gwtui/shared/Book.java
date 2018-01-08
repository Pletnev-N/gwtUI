package gwtui.shared;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {

    private int id;
    private String author;
    private String name;
    private int pages;
    private int publishYear;
    private Date addDate;

    public Book() {
    }

    public Book(int id, String author, String name, int pages, int publishYear, Date addDate) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.pages = pages;
        this.publishYear = publishYear;
        this.addDate = addDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", pages=" + pages +
                ", publishYear=" + publishYear +
                ", addDate=" + addDate +
                '}';
    }
}
