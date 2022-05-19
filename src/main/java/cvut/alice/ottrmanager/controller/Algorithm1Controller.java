package cvut.alice.ottrmanager.controller;

import org.springframework.web.bind.annotation.RestController;

//TODO: algo1
//TODO: algo2

@RestController
public class Algorithm1Controller {

//    @PostMapping(path = "/refactor")
//    public @ResponseBody Collection<RefactorResponse> refactorSentModel(@RequestBody String body, HttpServletResponse httpServletResponse) {
//        Model inputModel = ModelFactory.createDefaultModel();
//        Model templateModel = Manager.getManager().getDataset().getDefaultModel();
//        inputModel.read(new ByteArrayInputStream(body.getBytes()), null);
//        if (inputModel.isEmpty()) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
//        HashMap<ArrayList<Property>, ArrayList<Resource>> pairs = new HashMap<>();
//        ResIterator resIterator = inputModel.listSubjects();
//        while (resIterator.hasNext()) {
//            Resource subject = resIterator.nextResource();
//            StmtIterator stmtIterator = inputModel.listStatements(subject, null, (RDFNode) null);
//            ArrayList<Property> predicates = new ArrayList<>();
//            while (stmtIterator.hasNext()) {
//                Statement statement = stmtIterator.nextStatement();
//                predicates.add(statement.getPredicate());
//            }
//            predicates.sort(Comparator.comparing(Resource::getURI));
//            pairs.computeIfAbsent(predicates, k -> new ArrayList<>()).add(subject);
//        }
//
//    }
}
