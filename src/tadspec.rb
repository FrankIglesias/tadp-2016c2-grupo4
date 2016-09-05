
class TADsPec

  def method_missing(symbol, *args)
    "symbol -> #{symbol}\nargs -> #{args}"
  end

end