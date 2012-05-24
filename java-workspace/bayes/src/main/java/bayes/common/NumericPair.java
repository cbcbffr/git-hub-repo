/**
 * @author bing.cheng
 * @date 2012-5-2
 * @project tgrecom
 * @file GenericPair.java
 *
 */


package bayes.common;

public class NumericPair implements Cloneable{
	protected Number first;
	protected Number second;
	
	public NumericPair(Number first, Number second) {
		this.first = first;
		this.second = second;
	}

	public Number getFirst() {
		return first;
	}

	public void setFirst(Number first) {
		this.first = first;
	}

	public Number getSecond() {
		return second;
	}

	public void setSecond(Number second) {
		this.second = second;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static class KeyValuePair extends NumericPair implements Comparable<KeyValuePair>{
		public KeyValuePair(Integer first, Double second) {
			super(first, second);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((first == null) ? 0 : first.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NumericPair other = (NumericPair) obj;
			if (first == null) {
				if (other.first != null)
					return false;
			} else if (!first.equals(other.first))
				return false;
			return true;
		}

		public int compareTo(KeyValuePair other) {
			// TODO Auto-generated method stub
			return (Integer)this.first - (Integer)other.first;
		}
	}
	
	public static class KeyPair extends NumericPair {
		public KeyPair(Integer first, Integer second) {
			super(first, second);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((first == null) ? 0 : first.hashCode());
			result = prime * result + ((second == null) ? 0 : second.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NumericPair other = (NumericPair) obj;
			if (first == null) {
				if (other.first != null)
					return false;
			} else if (!first.equals(other.first))
				return false;
			if (second == null) {
				if (other.second != null)
					return false;
			} else if (!second.equals(other.second))
				return false;
			return true;
		}
	}
}
