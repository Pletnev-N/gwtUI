package gwtui.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gwtui.shared.Book;

import java.util.ArrayList;
import java.util.List;


@RemoteServiceRelativePath("books")
public interface BookService extends RemoteService {
    List<Book> getFirstPage();
    List<Book> addBook(Book book);
    List<Book> removeBook(int id);
    List<Book> getNextPage();
    List<Book> getPrevPage();
    int getPageNum();
    int getCurrPage();
    List<Book> sort(int columnNum);
}
