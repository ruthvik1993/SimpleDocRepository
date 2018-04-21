package apnacomplex.docrepository_anjaneya.model;

import java.util.ArrayList;

/**
 * Created by Ruthvik on 7/6/2017.
 */
public class DocCategoryModel {

    public ArrayList<InnerDocCategoryModel> getDocumentCategories() {
        return DocumentCategories;
    }

    public void setDocumentCategories(ArrayList<InnerDocCategoryModel> documentCategories) {
        DocumentCategories = documentCategories;
    }

    public ArrayList<InnerDocCategoryModel> DocumentCategories = new ArrayList<>();

    public class InnerDocCategoryModel {
        public String cat_id;
        public String cat_name;
        public String cat_icon;
        public String cat_background_img;
        public int num_docs;

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getCat_icon() {
            return cat_icon;
        }

        public void setCat_icon(String cat_icon) {
            this.cat_icon = cat_icon;
        }

        public int getNum_docs() {
            return num_docs;
        }

        public void setNum_docs(int num_docs) {
            this.num_docs = num_docs;
        }

        public String getCat_background_img() {
            return cat_background_img;
        }

        public void setCat_background_img(String cat_background_img) {
            this.cat_background_img = cat_background_img;
        }
    }

}
