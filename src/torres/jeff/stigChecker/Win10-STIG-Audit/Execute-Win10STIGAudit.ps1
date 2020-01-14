## Windows 10 STIG Compliance Audit
## Created by Trevor Bryant

$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Date = Get-date -format MMddyy
$Title = "Windows 10 Security Technical Implementation Guide"
$Fail = "C:\AutoSTIG\Win10-STIG-Audit\$Hostname-($Date)-failed.csv"
$Source = "C:\AutoSTIG\Win10-STIG-Audit\*.csv"
New-PSDrive -Name UNCPATH -PSProvider FileSystem -Root 
if ($Hostname -eq $null){

$Hostname = $env:computername}
if ($Hostname -ne $null){

$CATDir = "C:\AutoSTIG\Win10-STIG-Audit\CAT_I", "C:\AutoSTIG\Win10-STIG-Audit\CAT_II", "C:\AutoSTIG\Win10-STIG-Audit\CAT_III"
$Export = "C:\AutoSTIG\Win10-STIG-Audit\result\$Hostname-($Date)-result.csv"
}
if ($Hostname -eq $null){

$Hostname = $env:computername}

# Add logic to identify accurate working directory
(Get-ChildItem $CATDir).FullName | ForEach { & $_ } | Export-CSV -NoTypeInformation -Path $Export
Out-File -FilePath "C:\AutoSTIG\Win10-STIG-Audit\Result\done.txt"



