package gwtui.client;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import gwtui.shared.Book;

import java.util.Date;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtUi implements EntryPoint {

    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    private final BookServiceAsync bookService = GWT.create(BookService.class);
    private final Messages messages = GWT.create(Messages.class);

    private ListDataProvider dataProvider;

    private Label curPageLabel;
    private Label pageNumLabel;

    public void onModuleLoad() {

        final VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.addStyleName("mainPanel");
        final Button addBookButton = new Button("Add Book");
        addBookButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                final DialogBox dialogBox = new DialogBox();
                Button cancelButton = new Button("Cancel");
                Button addButton = new Button("Add");
                final TextBox authorTextBox = new TextBox();
                final TextBox nameTextBox = new TextBox();
                final TextBox pagesTextBox = new TextBox();
                final TextBox yearTextBox = new TextBox();

                Grid grid = new Grid(5, 2);
                dialogBox.setText("New Book");

                grid.setWidget(0,0, new Label("Author: "));
                grid.setWidget(0,1, authorTextBox);
                grid.setWidget(1,0, new Label("Name: "));
                grid.setWidget(1,1, nameTextBox);
                grid.setWidget(2,0, new Label("Pages: "));
                grid.setWidget(2,1, pagesTextBox);
                grid.setWidget(3,0, new Label("Year: "));
                grid.setWidget(3,1, yearTextBox);
                grid.setWidget(4,0, addButton);
                grid.setWidget(4,1, cancelButton);

                cancelButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        dialogBox.hide();
                    }
                });

                addButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        if (dataIsCorrect(authorTextBox, nameTextBox, pagesTextBox, yearTextBox)) {
                            Book book = new Book(0, authorTextBox.getText(), nameTextBox.getText(),
                                    Integer.parseInt(pagesTextBox.getText()), Integer.parseInt(yearTextBox.getText()), new Date());
                            bookService.addBook(book, new AsyncCallback<List<Book>>() {
                                @Override
                                public void onFailure(Throwable throwable) {}

                                @Override
                                public void onSuccess(List<Book> books) {
                                    updateTable(books);
                                }
                            });
                            dialogBox.hide();
                        }
                    }
                });

                dialogBox.setWidget(grid);
                dialogBox.center();
            }
        });
        mainPanel.add(addBookButton);

        bookService.getFirstPage(new AsyncCallback<List<Book>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(List<Book> books) {
                createTable(mainPanel, books);
                createPageChanger(mainPanel);
            }
        });

        RootPanel.get("gwtContainer").add(mainPanel);

    }

    private boolean dataIsCorrect(TextBox authorTextBox, TextBox nameTextBox, TextBox pagesTextBox, TextBox yearTextBox) {
        boolean result;
        try {
            int pages = Integer.parseInt(pagesTextBox.getText());
            if (pages <= 0) {
                result = false;
                pagesTextBox.addStyleName("incorrectField");
            }
            else {
                result = true;
                pagesTextBox.removeStyleName("incorrectField");
            }
        } catch (NumberFormatException e) {
            result = false;
            pagesTextBox.addStyleName("incorrectField");
        }

        try {
            int year = Integer.parseInt(yearTextBox.getText());
            if (year < 0) {
                result = false;
                yearTextBox.addStyleName("incorrectField");
            }
            else {
                result = true;
                yearTextBox.removeStyleName("incorrectField");
            }
        } catch (NumberFormatException e) {
            result = false;
            yearTextBox.addStyleName("incorrectField");
        }

        if (authorTextBox.getText().replaceAll(" ", "").equals("")) {
            result = false;
            authorTextBox.addStyleName("incorrectField");
        }
        else {
            result = true;
            authorTextBox.removeStyleName("incorrectField");
        }

        if (nameTextBox.getText().replaceAll(" ", "").equals("")) {
            result = false;
            nameTextBox.addStyleName("incorrectField");
        }
        else {
            result = true;
            nameTextBox.removeStyleName("incorrectField");
        }
        return result;
    }

    private void createTable(VerticalPanel panel, List<Book> booksList) {
        CellTable<Book> table = new CellTable<>();
        table.setStyleName("bookTable", true);
        table.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        NumberCell idCell = new NumberCell();
        Column<Book, Number> idColumn = new Column<Book, Number>(idCell) {
            @Override
            public Number getValue(Book book) {
                return book.getId();
            }
        };
        table.addColumn(idColumn, createHeader("Id", 0));

        TextColumn<Book> authorColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book book) {
                return book.getAuthor();
            }
        };
        table.addColumn(authorColumn, createHeader("Author", 1));

        TextColumn<Book> nameColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book book) {
                return book.getName();
            }
        };
        table.addColumn(nameColumn, createHeader("Name", 2));

        NumberCell pagesCell = new NumberCell();
        Column<Book, Number> pagesColumn = new Column<Book, Number>(pagesCell) {
            @Override
            public Number getValue(Book book) {
                return book.getPages();
            }
        };
        table.addColumn(pagesColumn, createHeader("Pages", 3));

        NumberCell yearCell = new NumberCell();
        Column<Book, Number> yearColumn = new Column<Book, Number>(yearCell) {
            @Override
            public Number getValue(Book book) {
                return book.getPublishYear();
            }
        };
        table.addColumn(yearColumn, createHeader("Year", 4));

        DateCell dateCell = new DateCell();
        Column<Book, Date> dateColumn = new Column<Book, Date>(dateCell) {
            @Override
            public Date getValue(Book book) {
                return book.getAddDate();
            }
        };
        table.addColumn(dateColumn, createHeader("Date of adding", 5));


        final SingleSelectionModel<Book> selectionModel = new SingleSelectionModel<Book>();
        table.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler( new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                Book selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    bookService.removeBook(selected.getId(), new AsyncCallback<List<Book>>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                        }

                        @Override
                        public void onSuccess(List<Book> books) {
                            updateTable(books);
                        }
                    });
                }
            }
        });



        table.setRowCount(booksList.size(), true);
        table.setRowData(0, booksList);
        table.setEmptyTableWidget(new Label(" No Books"));

        dataProvider = new ListDataProvider();
        dataProvider.addDataDisplay(table);
        updateTable(booksList);

        panel.add(table);
    }

    private Header<String> createHeader(String name, int colNum) {
        final int tmp = colNum;
        final String tmpStr = name;
        Header<String> header = new Header<String>(new ClickableTextCell()) {
            @Override
            public String getValue() {
                return tmpStr;
            }
        };
        header.setUpdater(new ValueUpdater<String>() {
            @Override
            public void update(String s) {
                bookService.sort(tmp, new AsyncCallback<List<Book>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(List<Book> books) {
                        updateTable(books);
                    }
                });
            }
        });
        return header;
    }

    private void createPageChanger(VerticalPanel panel) {
        HorizontalPanel pageChangerPanel = new HorizontalPanel();
        Button prevButton = new Button("<");
        Button nextButton = new Button(">");
        curPageLabel = new Label();
        pageNumLabel = new Label();
        Label slashLabel = new Label("/");

        curPageLabel.addStyleName("pageChangerLabel");
        pageNumLabel.addStyleName("pageChangerLabel");
        slashLabel.addStyleName("pageChangerLabel");

        prevButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                bookService.getPrevPage(new AsyncCallback<List<Book>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(List<Book> books) {
                        updateTable(books);
                    }
                });
            }
        });

        nextButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                bookService.getNextPage(new AsyncCallback<List<Book>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(List<Book> books) {
                        updateTable(books);
                    }
                });
            }
        });
        pageChangerPanel.add(prevButton);
        pageChangerPanel.add(curPageLabel);
        pageChangerPanel.add(slashLabel);
        pageChangerPanel.add(pageNumLabel);
        pageChangerPanel.add(nextButton);
        panel.add(pageChangerPanel);
    }

    private void updateTable(List<Book> books) {
        dataProvider.setList(books);
        dataProvider.refresh();
        bookService.getCurrPage(new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Integer integer) {
                curPageLabel.setText(Integer.toString(integer + 1));
            }
        });
        bookService.getPageNum(new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Integer integer) {
                pageNumLabel.setText(integer.toString());
            }
        });
    }

}
