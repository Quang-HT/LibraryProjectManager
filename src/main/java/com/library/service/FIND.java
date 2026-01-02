package com.library.service;

import com.library.dao.impl.*;
import com.library.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FIND implements FINDINFORMATION {

    @Override
    public String FindNameAuthor(int bookId) {

        Author_Book_DAO abDao = new Author_Book_DAO();
        Author_DAO authorDao = new Author_DAO();

        List<Author_Book_Entity> list = abDao.findAuthors_BookByBookId(bookId);

        if (list == null || list.isEmpty()) {
            return "Không rõ tác giả";
        }

        List<String> names = new ArrayList<>();

        for (Author_Book_Entity ab : list) {
            authorDao.findById(ab.getAuthor_id())
                    .map(Author_Entity::getAuthor_name)
                    .ifPresent(names::add);
        }

        if (names.isEmpty()) return "Không rõ tác giả";

        return String.join(", ", names);
    }


    @Override
    public String FindNameCategory(int book_id) {
        Book_Category_DAO Book_CategoryDao = new Book_Category_DAO();
        Category_DAO CategoryDao = new Category_DAO();

        Optional<Book_Category_Entity> ttlOpt = Book_CategoryDao.findById(book_id);
        if (ttlOpt.isEmpty()) {
            return "Không rõ thể loại";
        }

        Book_Category_Entity Book_Category = ttlOpt.get();

        Optional<Category_Entity> tlOpt = CategoryDao.findById(Book_Category.getCategory_id());
        return tlOpt
                .map(Category_Entity::getCategory_name)
                .orElse("Không rõ thể loại");
    }

    @Override
    public String FindNamePublisher(int book_id) {
        Book_DAO BookDao = new Book_DAO();
        Publisher_DAO nxbDao = new Publisher_DAO();

        Optional<Book_Entity> BookOpt = BookDao.findById(book_id);
        if (BookOpt.isEmpty()) {
            return "Không rõ Publisher";
        }

        Book_Entity Book = BookOpt.get();
        Integer publisher_id = Book.getPublisher_id();

        if (publisher_id == null) {
            return "Không rõ Publisher";
        }

        Optional<Publisher_Entity> nxbOpt = nxbDao.findById(publisher_id);
        return nxbOpt
                .map(Publisher_Entity::getPublisher_name)
                .orElse("Không rõ Publisher");
    }

    @Override
    public String FindLocation(int book_id) {
        Copy_DAO dao = new Copy_DAO();

        Optional<Copy_Entity> op = dao.findById(book_id);
        Copy_Entity copy= op.get();
        return copy.getLocation();
    }


    @Override
    public double FindCost(int book_id) {
        Copy_DAO dao = new Copy_DAO();

        return dao.findById(book_id)
                .map(Copy_Entity::getCost)
                .orElse(0);
    }

    @Override
    public Optional<Account_Entity> findAccountByStaffEmail(String email) {
        Staff_DAO staffDao = new Staff_DAO();
        Account_DAO accountDao = new Account_DAO();
        Optional<Staff_Entity> staffOpt = staffDao.findEmail(email);
        if (staffOpt.isEmpty()) {
            return Optional.empty();
        }
        Integer staffId = staffOpt.get().getStaff_id();
        return accountDao.findById(staffId);
    }

    @Override
    public int countBorrowed(){
        List<Copy_Entity> list=new Copy_DAO().FindBorrow();
        return list.size();
    }

    @Override
    public int countCopy(int idBook){
        List<Copy_Entity> list = new Copy_DAO().findByIdBook(idBook);
        return list.size();
    }

    @Override
    public int countBorrowByIdBook(int idBook){
        List<Copy_Entity> listC = new Copy_DAO().findByIdBook(idBook);
        int count = 0;
        for(Copy_Entity c : listC){
            List<Copy_BorrowSlip_Entity> listCB = new Copy_BorrowSlip_DAO().findByIdCopy(c.getCopy_id());
            count += listCB.size();
        }
        return count;
    }
}




