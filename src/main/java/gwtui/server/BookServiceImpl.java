package gwtui.server;

import gwtui.client.BookService;
import gwtui.shared.Book;
import gwtui.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.*;
import java.util.*;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookServiceImpl extends RemoteServiceServlet implements BookService {

    //private List<Book> books;
    private int currPage;
    private int pageNum;
    private final int ROWS_PER_PAGE = 10;

    public BookServiceImpl() {
        super();
        currPage = 0;
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(1, "author1", "name1", 450, 1956, new Date(563475666)));
        books.add(new Book(2, "author2", "name2", 1050, 1975, new Date(123214)));
        books.add(new Book(3, "author3", "name3", 50, 1956, new Date(System.currentTimeMillis() / 3)));
        books.add(new Book(4, "author4", "name4", 100, 2003, new Date(12324414)));
        books.add(new Book(5, "author5", "name5", 100, 1993, new Date(915360000)));
        books.add(new Book(6, "author6", "name6", 1400, 1953, new Date(System.currentTimeMillis() - 472748214)));
        books.add(new Book(7, "author7", "name7", 560, 1985, new Date(334190966)));
        books.add(new Book(8, "author8", "name8", 100, 1935, new Date(1515813233)));
        books.add(new Book(9, "author9", "name9", 826, 1985, new Date(915399900)));
        books.add(new Book(10, "author10", "name10", 72, 2011, new Date(System.currentTimeMillis() - 763654214)));
        books.add(new Book(11, "author11", "name11", 450, 1958, new Date(1815813233)));
        books.add(new Book(12, "author12", "name12", 992, 1936, new Date(System.currentTimeMillis() / 2)));
        pageNum = books.size()/ROWS_PER_PAGE + 1;
        writeFile(books);
    }

    private List<Book> getBooks(List<Book> books, int page) {
        List<Book> list = new ArrayList<>();
        list.addAll(books.subList( page*ROWS_PER_PAGE, Math.min((page+1)*ROWS_PER_PAGE, books.size()) ));
        return list;
    }

    @Override
    public List<Book> getFirstPage() {
        ArrayList<Book> books = readFile();
        return getBooks(books, 0);
    }

    @Override
    public List<Book> addBook(Book book) {
        ArrayList<Book> books = readFile();
        book.setId(findNewId(books));
        books.add(book);
        pageNum = books.size()/ROWS_PER_PAGE + 1;
        writeFile(books);
        return getBooks(books, currPage);
    }

    @Override
    public List<Book> removeBook(int id) {
        ArrayList<Book> books = readFile();
        for (Book book: books) {
            if (book.getId() == id) {
                books.remove(book);
                break;
            }
        }
        pageNum = books.size()/ROWS_PER_PAGE + 1;
        if (currPage == pageNum) currPage--;
        writeFile(books);
        return getBooks(books, currPage);
    }

    @Override
    public List<Book> getNextPage() {
        ArrayList<Book> books = readFile();
        if (currPage != pageNum-1) currPage++;
        return getBooks(books, currPage);
    }

    @Override
    public List<Book> getPrevPage() {
        ArrayList<Book> books = readFile();
        if (currPage != 0) currPage--;
        return getBooks(books, currPage);
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public int getCurrPage() {
        return currPage;
    }

    @Override
    public List<Book> sort(int columnNum) {
        ArrayList<Book> books = readFile();
        switch (columnNum) {
            case 0:
                Collections.sort(books, new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        if (o1 == o2) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getId() - o2.getId() : 1;
                        }
                        return -1;
                    }
                });
                break;
            case 1:
                Collections.sort(books, new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        if (o1 != null) {
                            return (o2 != null) ? o1.getAuthor().compareTo(o2.getAuthor()) : 1;
                        }
                        return -1;
                    }
                });
                break;
            case 2:
                Collections.sort(books, new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        if (o1 != null) {
                            return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
                        }
                        return -1;
                    }
                });
                break;
            case 3:
                Collections.sort(books, new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        if (o1 != null) {
                            return (o2 != null) ? o1.getPages()-o2.getPages() : 1;
                        }
                        return -1;
                    }
                });
                break;
            case 4:
                Collections.sort(books, new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        if (o1 == o2) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getPublishYear() - o2.getPublishYear() : 1;
                        }
                        return -1;
                    }
                });
                break;
            case 5:
                Collections.sort(books, new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        if (o1 != null) {
                            return (o2 != null) ? o1.getAddDate().compareTo(o2.getAddDate()) : 1;
                        }
                        return -1;
                    }
                });
                break;
        }
        writeFile(books);
        return getBooks(books, currPage);
    }

    private int findNewId(ArrayList<Book> books) {
        int result = 0;
        for (int i = 1; i < books.size(); i++) {
            if (books.get(i).getId() > result) result = books.get(i).getId();
        }
        return result + 1;
    }

    private void writeFile(ArrayList<Book> books) {
        try {
            FileOutputStream fout = new FileOutputStream("library");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Book> readFile() {
        try {
            FileInputStream fin = new FileInputStream("library");
            ObjectInputStream ois = new ObjectInputStream(fin);
            return (ArrayList<Book>)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
