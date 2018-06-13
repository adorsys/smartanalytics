package de.adorsys.smartanalytics.utils;

import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.matcher.ExpressionMatcher;
import de.adorsys.smartanalytics.matcher.SimilarityMatcher;
import de.adorsys.smartanalytics.rule.syntax.ExpressionLexer;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringReader;

@Slf4j
public class RulesFactory {

    public static Matcher createExpressionMatcher(String expression) {
        Rule rule = new Rule();
        rule.setExpression(expression);

        return createExpressionMatcher(rule);
    }

    public static SimilarityMatcher createSimilarityMatcher(Rule rule) {
        return new SimilarityMatcher(rule);
    }

    public static ExpressionMatcher createExpressionMatcher(Rule rule) {
        ExpressionParser.ExpressionContext ruleContext = null;

        if (StringUtils.isNotBlank(rule.getExpression())) {
            try {
                CharStream input = CharStreams.fromReader(new StringReader(rule.getExpression()));
                ExpressionLexer lexer = new ExpressionLexer(input);
                lexer.removeErrorListeners();
                lexer.addErrorListener(new ConsoleErrorListener() {
                    @Override
                    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                        log.warn("line " + line + ":" + charPositionInLine + " " + msg);
                        log.warn(input.toString());
                    }
                });

                ExpressionParser parser = new ExpressionParser(new CommonTokenStream(lexer));
                ruleContext = parser.expression();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new ExpressionMatcher(ruleContext, rule);
    }

}
