package org.lareferencia.backend.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.validator.ContentValidatorResult;
import org.lareferencia.backend.validation.validator.QuantifierValues;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;

public class FieldExpressionEvaluator extends AbstractEvaluator<Boolean> {

	/** The negate unary operator. */
	public final static Operator NEGATE = new Operator("NOT", 1, Operator.Associativity.RIGHT, 3);
	/** The logical AND operator. */
	private static final Operator AND = new Operator("AND", 2, Operator.Associativity.LEFT, 2);
	/** The logical OR operator. */
	public final static Operator OR = new Operator("OR", 2, Operator.Associativity.LEFT, 1);

	public final static Pattern PATTERN = Pattern.compile("(.+)(==|=%)'(.*)'");
	public final static Pattern TOKENIZER_PATTERN = Pattern.compile("\\(|\\)|[^\\s']+?=='[^']*'|[^\\s']+");

	private static final Parameters PARAMETERS;

	static {
		// Create the evaluator's parameters
		PARAMETERS = new Parameters();
		// Add the supported operators
		PARAMETERS.add(AND);
		PARAMETERS.add(OR);
		PARAMETERS.add(NEGATE);

		PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
	}

	private QuantifierValues quantifier;

	public FieldExpressionEvaluator(QuantifierValues quantifier) {
		super(PARAMETERS);
		this.quantifier = quantifier;
	}

	@Getter
	List<ContentValidatorResult> evaluationResults;

	@Override
	public Boolean evaluate(String expression, Object evaluationContext) {
		evaluationResults = new ArrayList<ContentValidatorResult>();
		return super.evaluate(expression, evaluationContext);
	};

	@Override
	protected Boolean toValue(String literal, Object metadataObject) {

		OAIRecordMetadata metadata = (OAIRecordMetadata) metadataObject;

		Matcher matcher = PATTERN.matcher(literal);

		boolean match = matcher.matches();
		int groupCount = matcher.groupCount();

		if (match && groupCount == 3) {

			String fieldName = matcher.group(1);
			String testValue = matcher.group(3);
			String operator = matcher.group(2);

			int validOccurrencesCount = 0;

			int occurrencesSize = 0;
			
			
			List<String> occrs =  metadata.getFieldOcurrences(fieldName);
			

			for (String fieldValue :occrs) {

				occurrencesSize++;

				ContentValidatorResult result = new ContentValidatorResult();
				result.setReceivedValue(fieldName + " :: " + testValue + " " + operator + " " + fieldValue);
				result.setValid(false);

				switch (operator) {

				case "==": /* caso igualdad strings */
					if (testValue.equals(fieldValue)) {
						result.setValid(true);
						validOccurrencesCount++;
					}
					break;

				case "=%": /* caso expresiones regulares */
					if (Pattern.matches(testValue, fieldValue)) {
						result.setValid(true);
						validOccurrencesCount++;
					}
					break;

				default:
					break;
				}

				//evaluationResults.add(result);

			}
			
			// SI NO HAY OCCRS LO INDICA COMO UN VALOR DE RESULTADO
			if ( occrs.size() == 0 ) {
				ContentValidatorResult result = new ContentValidatorResult();
				result.setReceivedValue(fieldName + " :: " + "OCCRS = 0");
				result.setValid(false);
				evaluationResults.add(result);
			}

			/*
			 * de acuerdo al cuantificador y la cantidad de reglas válidas
			 * decide si la reglas es válida
			 */
			switch (quantifier) {

			case ONE_ONLY:
				return validOccurrencesCount == 1;

			case ONE_OR_MORE:
				return validOccurrencesCount >= 1;

			case ZERO_OR_MORE:
				return validOccurrencesCount >= 0;

			case ZERO_ONLY:
				return validOccurrencesCount == 0;

			case ALL:
				return validOccurrencesCount == occurrencesSize;

			default:
				return false;

			}

		} else {

			System.err.println("Error en la expresión de regla: " + literal);
			return false;
		}

	}

	@Override
	protected Boolean evaluate(Operator operator, Iterator<Boolean> operands, Object metadataObject) {
		if (operator == NEGATE) {
			return !operands.next();
		} else if (operator == OR) {
			Boolean o1 = operands.next();
			Boolean o2 = operands.next();
			return o1 || o2;
		} else if (operator == AND) {
			Boolean o1 = operands.next();
			Boolean o2 = operands.next();
			return o1 && o2;
		} else {
			return super.evaluate(operator, operands, metadataObject);
		}
	}

	@Override
	protected Iterator<String> tokenize(String expression) {
	
		 List<String> tokens = new ArrayList<String>();
		 Matcher m = TOKENIZER_PATTERN.matcher(expression);   // get a matcher object

	      while(m.find()) {
	         String token = expression.substring(m.start(), m.end() );
	         tokens.add(token);
	         ///System.out.println(token);
	      }
		
		return tokens.iterator();
	}

}
