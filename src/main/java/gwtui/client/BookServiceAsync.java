package gwtui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwtui.shared.Book;

import java.util.ArrayList;
import java.util.List;

public interface BookServiceAsync
{
    void getNextPage(AsyncCallback<List<Book>> async);

    void getPrevPage(AsyncCallback<List<Book>> async);

    void addBook(Book book, AsyncCallback<List<Book>> async);

    void removeBook(int id, AsyncCallback<List<Book>> async);

    void getPageNum(AsyncCallback<Integer> async);

    void getCurrPage(AsyncCallback<Integer> async);

    void getFirstPage(AsyncCallback<List<Book>> async);

    void sort(int columnNum, AsyncCallback<List<Book>> async);

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util
    {
        private static BookServiceAsync instance;

        public static final BookServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (BookServiceAsync) GWT.create( BookService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
