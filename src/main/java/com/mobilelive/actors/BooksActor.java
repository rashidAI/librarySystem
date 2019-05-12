package com.mobilelive.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import com.mobilelive.messages.BookMessages;
import com.mobilelive.messages.UserMessages;
import com.mobilelive.service.BookService;
import com.mobilelive.service.StudentService;
import com.mobilelive.service.UserService;

public class BooksActor extends AbstractActor{

    private BookService bookService = new BookService();

    public static Props props() {
        return Props.create(BooksActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(BookMessages.AddBookMessage.class, handleAddBook())
                .match(BookMessages.GetBookMessage.class, handleGetBook())
                .match(BookMessages.GetBooksMessage.class, handleGetBooks())
                .match(BookMessages.IssueBookMessage.class, handleIssueBooks())
                .match(BookMessages.ReturnBookMessage.class, handleReturnBooks())
                .match(BookMessages.DeleteBookMessage.class, handleDeleteBook())
                .match(BookMessages.UpdateBookMessage.class, handleUpdateBook())
                .build();
    }

    private FI.UnitApply<BookMessages.AddBookMessage> handleAddBook() {
        return createUserMessage -> {
            bookService.addBook(createUserMessage.getBook());
            sender()
                    .tell(new BookMessages.ActionPerformed(bookService.addBook(createUserMessage.getBook())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.GetBookMessage> handleGetBook() {
        return getUserMessage -> {
            sender().tell(bookService.getBook(getUserMessage.getBookId()), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.GetBooksMessage> handleGetBooks() {
        return getUserMessage -> {
            sender().tell(bookService.getBooks(), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.IssueBookMessage> handleIssueBooks() {
        return issueBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(bookService.issueBook(issueBookMessage.getStudentToIssueBook())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.ReturnBookMessage> handleReturnBooks() {
        return issueBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(bookService.returnBook( issueBookMessage.getStudentToReturnBook())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.DeleteBookMessage> handleDeleteBook() {
        return deleteBookMessage -> {
            sender().tell(bookService.deleteBook(deleteBookMessage.getBookId()), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.UpdateBookMessage> handleUpdateBook() {
        return updateBookMessage -> {
            bookService.updateBook( updateBookMessage.getBook());
            sender().tell(new UserMessages.ActionPerformed(
                    String.format("User %s updated.", updateBookMessage.getBook().getName())), getSelf());
        };
    }
}
