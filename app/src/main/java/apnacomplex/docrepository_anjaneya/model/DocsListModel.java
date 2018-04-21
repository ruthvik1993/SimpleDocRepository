package apnacomplex.docrepository_anjaneya.model;

import java.util.ArrayList;

/**
 * Created by prakh on 7/7/2017.
 */

public class DocsListModel {

    public ArrayList<DocumentsModel> documents = new ArrayList<>();

    public ArrayList<DocumentsModel> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<DocumentsModel> documents) {
        this.documents = documents;
    }

    public class DocumentsModel {
        public String doc_name, doc_size, doc_type, doc_url;

        public String getDoc_name() {
            return doc_name;
        }

        public void setDoc_name(String doc_name) {
            this.doc_name = doc_name;
        }

        public String getDoc_size() {
            return doc_size;
        }

        public void setDoc_size(String doc_size) {
            this.doc_size = doc_size;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public String getDoc_url() {
            return doc_url;
        }

        public void setDoc_url(String doc_url) {
            this.doc_url = doc_url;
        }
    }
}
