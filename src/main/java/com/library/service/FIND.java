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

        // Lấy tất cả dòng Author_Book theo bookId
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

        // Gộp nhiều tác giả lại thành 1 chuỗi
        return String.join(", ", names);
    }


    @Override
    public String FindNameCategory(int book_id) {
        Book_Category_DAO Book_CategoryDao = new Book_Category_DAO();
        Category_DAO CategoryDao = new Category_DAO();

        // Giả sử findById(book_id) trả về 1 dòng Book_Category theo book_id
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

        // BƯỚC 1: tìm sách theo book_id
        Optional<Book_Entity> BookOpt = BookDao.findById(book_id);
        if (BookOpt.isEmpty()) {
            return "Không rõ Publisher";
        }

        Book_Entity Book = BookOpt.get();
        Integer publisher_id = Book.getPublisher_id();  // nhớ trong entity phải có field này

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
        copy_DAO dao = new copy_DAO();

        Optional<copy_Entity> op = dao.findById(book_id);
        copy_Entity copy= op.get();
        return copy.getLocation();
    }


    @Override
    public double FindCost(int book_id) {
        copy_DAO dao = new copy_DAO();

        return dao.findById(book_id)
                .map(copy_Entity::getCost)
                .orElse(0);
    }

    public Optional<Account_Entity> findAccountByStaffEmail(String email) {
        Staff_DAO staffDao = new Staff_DAO();
        Account_DAO accountDao = new Account_DAO();
        Optional<Staff_Entity> staffOpt = staffDao.findEmail(email);
        if (staffOpt.isEmpty()) {
            return Optional.empty();
        }
        Integer staffId = staffOpt.get().getStaff_id();  // account_id = staff_id
        return accountDao.findById(staffId);
    }

    public int countBorrowed(){
        List<copy_Entity> list=new copy_DAO().FindBorrow();
        return list.size();
    }
    public int countCopy(int idBook){
        List<copy_Entity> list = new copy_DAO().findByIdBook(idBook);
        return list.size();
    }

    public int countBorrowByIdBook(int idBook){
        List<copy_Entity> listC = new copy_DAO().findByIdBook(idBook);
        int count = 0;
        for(copy_Entity c : listC){
            List<Copy_BorrowSlip_Entity> listCB = new Copy_BorrowSlip_DAO().findByIdCopy(c.getCopy_id());
            count += listCB.size();
        }
        return count;
    }
}




