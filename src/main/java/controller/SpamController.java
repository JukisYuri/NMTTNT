package controller;

import model.DecisionTree;
import view.MailFilterFrame;

public class SpamController {
    MailFilterFrame view = new MailFilterFrame();
    DecisionTree model = new DecisionTree();

    public SpamController(MailFilterFrame view, DecisionTree model) {
        this.view = view;
        this.model = model;
    }
}
